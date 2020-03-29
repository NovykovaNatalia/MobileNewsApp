package com.natlight.mobilenewsapp.services;

import com.natlight.mobilenewsapp.Model.News;
import com.natlight.mobilenewsapp.Model.Post;
import com.natlight.mobilenewsapp.Model.WebSite;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

import static com.natlight.mobilenewsapp.MainActivity.API_KEY;

public interface NewsApi {
  @GET("v2/sources?language=en&apiKey="+ API_KEY)
    Call<WebSite>getSources();
  @GET
    Call<News> getNewestArticles(@Url String url);
}
