package com.natlight.mobilenewsapp;

import android.support.v4.widget.SwipeRefreshLayout;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.natlight.mobilenewsapp.Model.WebSite;
import com.natlight.mobilenewsapp.adapter.SourceAdapter;
import com.natlight.mobilenewsapp.services.NetworkService;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import retrofit2.Callback;
import retrofit2.Response;

public class SourceActivity extends AppCompatActivity {
    RecyclerView listWebsite;
    RecyclerView.LayoutManager layoutManager;
    NetworkService mSevice;
    SourceAdapter adapter;
    AlertDialog dialog;
    SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);

        //Create Cache
        Paper.init(this);

        mSevice = NetworkService.getInstance();
        swipeLayout = findViewById(R.id.swipeRefresh);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWebsiteSource(true);
            }
        });

        listWebsite = (RecyclerView)findViewById(R.id.list_source);
        listWebsite.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listWebsite.setLayoutManager(layoutManager);

        dialog = new SpotsDialog(this);

        loadWebsiteSource(false);
    }

    private void loadWebsiteSource(boolean isRefreshed) {
        if (!isRefreshed) {
//            Toast.makeText(getBaseContext(), "!isRefreshed", Toast.LENGTH_LONG).show();
            String cache = Paper.book().read("cache");
            if (cache != null && !cache.isEmpty() && !cache.equals("null")) {
                WebSite webSite = new Gson().fromJson(cache, WebSite.class);
                adapter = new SourceAdapter(getBaseContext(), webSite);
                adapter.notifyDataSetChanged();
                listWebsite.setAdapter(adapter);
            } else {
//                Toast.makeText(getBaseContext(), "!isRefreshed else", Toast.LENGTH_LONG).show();
                dialog.show();
                mSevice.getNewsJSONApi().getSources().enqueue(new Callback<WebSite>() {
                    @Override
                    public void onResponse(retrofit2.Call<WebSite> call, Response<WebSite> response) {
                        adapter = new SourceAdapter(getBaseContext(), response.body());
                        adapter.notifyDataSetChanged();
                        listWebsite.setAdapter(adapter);

                        Paper.book().write("cache", new Gson().toJson(response.body()));
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(retrofit2.Call<WebSite> call, Throwable t) {
                        //TODO: create some messages in all onFailure methods.
                    }
                });
            }
        } else {
            swipeLayout.setRefreshing(true);

            mSevice.getNewsJSONApi().getSources().enqueue(new Callback<WebSite>() {
                @Override
                public void onResponse(retrofit2.Call<WebSite> call, Response<WebSite> response) {
                    adapter = new SourceAdapter(getBaseContext(), response.body());
                    adapter.notifyDataSetChanged();
                    listWebsite.setAdapter(adapter);

                    Paper.book().write("cache", new Gson().toJson(response.body()));

                    swipeLayout.setRefreshing(false);
                    Toast.makeText(getBaseContext(), " else", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(retrofit2.Call<WebSite> call, Throwable t) {
                }
            });
        }
    }
}


