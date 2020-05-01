package com.natlight.mobilenewsapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.diagonallayout.DiagonalLayout;
import com.natlight.mobilenewsapp.Model.Article;
import com.natlight.mobilenewsapp.Model.News;
import com.natlight.mobilenewsapp.adapter.NewsAdapter;
import com.natlight.mobilenewsapp.services.NetworkService;
import com.squareup.picasso.Picasso;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity {
    public static final String API_KEY = "017ac0ad7b2c4367ba9e0a08343d8e9b";
    final int DIALOG_EXIT = 1;
    final String LOG_TAG = "My logs";
    KenBurnsView kbv;
    DiagonalLayout diagonalLayout;
    NetworkService networkService;
    TextView topAuthor;
    TextView topTitle;
    SwipeRefreshLayout swipeRefreshLayout;
    Context ctx;
    ProgressBar progressBar;
    Handler handler;

    String source = "";
    String sortBy = "";
    String webHotURL = "";

    NewsAdapter adapter;
    RecyclerView lstNews;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        /*Declare and initialize variables*/
        diagonalLayout = findViewById(R.id.diagonalLayout);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_source);
        progressBar = findViewById(R.id.SpinKitNews);
        kbv = findViewById(R.id.top_image);
        topAuthor = findViewById(R.id.top_author);
        topTitle = findViewById(R.id.top_title);
        lstNews = findViewById(R.id.lstNews);

        ctx = this;
        networkService = NetworkService.getInstance();
        layoutManager = new LinearLayoutManager(this);

        /* Configuration block*/
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews(source, true);
            }
        });

        diagonalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detail = new Intent(getBaseContext(), ArticleActivity.class);
                detail.putExtra("webURL", webHotURL);
                startActivity(detail);
            }
        });
        lstNews.setHasFixedSize(true);
        lstNews.setLayoutManager(layoutManager);

        if (getIntent() != null) {
            source = getIntent().getStringExtra("source");
            if (!source.isEmpty()) {
                loadNews(source, false);
            }
        }
    }

    private void loadNews(String source, boolean isRefreshed) {
        //TODO: implement cache feature
        networkService.getNewsJSONApi()
            .getNewestArticles(networkService.getAPIUrl(source, API_KEY))
            .enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    progressBar.setVisibility(View.GONE);
                    Picasso.get()
                            .load(response.body().getArticles().get(0).getUrlToImage())
                            .into(kbv);
                    topTitle.setText(response.body().getArticles().get(0).getTitle());
                    topAuthor.setText(response.body().getArticles().get(0).getAutor());
                    webHotURL = response.body().getArticles().get(0).getUrl();

                    List<Article> removeFirstItem = response.body().getArticles();
                    removeFirstItem.remove(0);
                    adapter = new NewsAdapter(removeFirstItem, getBaseContext());
                    adapter.notifyDataSetChanged();
                    lstNews.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("mobileNewsApp", String.format("Request failed. URL: %s ",
                            networkService.getAPIUrl(source, API_KEY)));

                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setMessage("Request failed. please try again")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });
                    builder.create()
                            .show();
                }
            });
    }
}
