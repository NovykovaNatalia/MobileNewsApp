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
import android.widget.SearchView;

import com.google.gson.Gson;
import com.natlight.mobilenewsapp.Model.NewsSources;
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
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);
        /* Declare and initialize variables*/
        ctx = this;
        //Create Cache
        Paper.init(ctx);

        networkService = NetworkService.getInstance();
        swipeLayout = findViewById(R.id.swipe_refresh_source);
        listWebsite = findViewById(R.id.list_source);
        layoutManager = new LinearLayoutManager(ctx);
        searchView = findViewById(R.id.search_view);

        /* End block of declare an initialization*/

        /* Configuration block*/
        listWebsite.setHasFixedSize(true);
        listWebsite.setLayoutManager(layoutManager);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

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
                NewsSources newsSources = new Gson().fromJson(cache, NewsSources.class);
                createAdapter(newsSources);
            } else {
                handleRequest();
            }
        } else {
            swipeLayout.setRefreshing(true);
            handleRequest();
            swipeLayout.setRefreshing(false);
        }
    }

    private void createAdapter(NewsSources newsSources) {
        adapter = new SourceAdapter(ctx, newsSources);
        adapter.notifyDataSetChanged();
        listWebsite.setAdapter(adapter);
    }

    private void handleFailure() {
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

    private void handleRequest() {
        progressBar = findViewById(R.id.SpinKitSource);
        networkService.getNewsJSONApi().getSources().enqueue(new Callback<NewsSources>() {
            @Override
            public void onResponse(retrofit2.Call<NewsSources> call, Response<NewsSources> response) {
                createAdapter(response.body());
                Paper.book().write("cache", new Gson().toJson(response.body()));
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(retrofit2.Call<NewsSources> call, Throwable t) {
                handleFailure();
            }
        });
    }
}


