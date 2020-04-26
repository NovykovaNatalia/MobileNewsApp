package com.natlight.mobilenewsapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
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
    NetworkService mService;
    TextView top_author;
    TextView top_title;
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

        progressBar = findViewById(R.id.SpinKitNews);
        ctx = this;
        mService = NetworkService.getInstance();

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews(source, true);
            }
        });

        diagonalLayout = findViewById(R.id.diagonalLayout);
        diagonalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detail = new Intent(getBaseContext(), ArticleActivity.class);
                detail.putExtra("webURL", webHotURL);
                startActivity(detail);
            }
        });
        kbv = findViewById(R.id.top_image);
        top_author = findViewById(R.id.top_author);
        top_title = findViewById(R.id.top_title);

        lstNews = findViewById(R.id.lstNews);
        lstNews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lstNews.setLayoutManager(layoutManager);

        if (getIntent() != null) {
            source = getIntent().getStringExtra("source");
            if (!source.isEmpty()) {
                loadNews(source, false);
            }
        }
    }


    private void loadNews(String source, boolean isRefreshed) {
        if (!isRefreshed) {
            Log.e("DNOVYKOV", mService.getAPIUrl(source, API_KEY));
            mService.getNewsJSONApi().getNewestArticles(mService.getAPIUrl(source, API_KEY))
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            progressBar.setVisibility(View.GONE);
                            Picasso.get()
                                    .load(response.body().getArticles().get(0).getUrlToImage())
                                    .into(kbv);
                            top_title.setText(response.body().getArticles().get(0).getTitle());
                            top_author.setText(response.body().getArticles().get(0).getAutor());
                            webHotURL = response.body().getArticles().get(0).getUrl();

                            List<Article> removeFirstItem = response.body().getArticles();
                            removeFirstItem.remove(0);
                            adapter = new NewsAdapter(removeFirstItem, getBaseContext());
                            adapter.notifyDataSetChanged();
                            lstNews.setAdapter(adapter);
                            Log.e(LOG_TAG, "Before failure");
                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            Log.e("mobileNewsApp", String.format("Request failed. URL: %s ",
                                    mService.getAPIUrl(source, API_KEY)));

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
        } else {
            mService.getNewsJSONApi().getNewestArticles(mService.getAPIUrl(source, API_KEY))
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            progressBar.setVisibility(View.GONE);
                            Picasso.get()
                                    .load(response.body().getArticles().get(0).getUrlToImage())
                                    .into(kbv);

                            top_title.setText(response.body().getArticles().get(0).getTitle());
                            top_author.setText(response.body().getArticles().get(0).getAutor());

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
                                    mService.getAPIUrl(source, API_KEY)));

                            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                            builder.setMessage("Request failed. please try again")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            finish();
                                        }
                                    });
                            builder
                                    .create()
                                    .show();

                        }
                    });
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}
