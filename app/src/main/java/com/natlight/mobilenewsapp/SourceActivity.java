package com.natlight.mobilenewsapp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.gson.Gson;
import com.natlight.mobilenewsapp.Model.WebSite;
import com.natlight.mobilenewsapp.adapter.SourceAdapter;
import com.natlight.mobilenewsapp.services.NetworkService;
import io.paperdb.Paper;
import retrofit2.Callback;
import retrofit2.Response;

public class SourceActivity extends AppCompatActivity {
    RecyclerView listWebsite;
    RecyclerView.LayoutManager layoutManager;
    NetworkService networkService;
    SourceAdapter adapter;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeLayout;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);
        /* Declare and initialize variables*/
        ctx = this;
        //Create Cache
        Paper.init(ctx);

        networkService = NetworkService.getInstance();
        swipeLayout = findViewById(R.id.swipeRefresh);
        listWebsite = findViewById(R.id.list_source);
        layoutManager = new LinearLayoutManager(ctx);

        /* End block of declare an initialization*/

        /* Configuration block*/
        listWebsite.setHasFixedSize(true);
        listWebsite.setLayoutManager(layoutManager);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWebsiteSource(true);
            }
        });
        /* End of Configuration block*/

        loadWebsiteSource(false);
    }

    private void loadWebsiteSource(boolean isRefreshed) {
        if (!isRefreshed) {
            String cache = Paper.book().read("cache");
            if (cache != null && !cache.isEmpty() && !cache.equals("null")) {
                WebSite webSite = new Gson().fromJson(cache, WebSite.class);
                adapter = new SourceAdapter(getBaseContext(), webSite);
                adapter.notifyDataSetChanged();
                listWebsite.setAdapter(adapter);
            } else {
                progressBar = findViewById(R.id.SpinKitSource);

                networkService.getNewsJSONApi().getSources().enqueue(new Callback<WebSite>() {
                    @Override
                    public void onResponse(retrofit2.Call<WebSite> call, Response<WebSite> response) {
                        adapter = new SourceAdapter(getBaseContext(), response.body());
                        adapter.notifyDataSetChanged();
                        listWebsite.setAdapter(adapter);

                        Paper.book().write("cache", new Gson().toJson(response.body()));
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(retrofit2.Call<WebSite> call, Throwable t) {
                        //TODO: create some messages in all onFailure methods.
                        progressBar.setVisibility(View.GONE);
                        Log.e("MobileNewsApp", "Unable rich sources");
                        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                        builder.setMessage("Request failed. please try again")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                });
                        builder.create().show();
                    }
                });
            }
        } else {
            swipeLayout.setRefreshing(true);

            networkService.getNewsJSONApi().getSources().enqueue(new Callback<WebSite>() {
                @Override
                public void onResponse(retrofit2.Call<WebSite> call, Response<WebSite> response) {
                    adapter = new SourceAdapter(getBaseContext(), response.body());
                    adapter.notifyDataSetChanged();
                    listWebsite.setAdapter(adapter);

                    Paper.book().write("cache", new Gson().toJson(response.body()));

                    swipeLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(retrofit2.Call<WebSite> call, Throwable t) {
                    Log.e("MobileNewsApp", "Unable rich sources");
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setMessage("Request failed. please try again")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });
                    builder.create().show();
                }
            });
        }
    }
}


