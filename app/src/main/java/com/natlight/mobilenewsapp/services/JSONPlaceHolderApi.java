package com.natlight.mobilenewsapp.services;

import com.natlight.mobilenewsapp.Model.Post;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JSONPlaceHolderApi {

    @GET("/posts/{id}")
    Call<Post> getPostWithID(@Path("id") int id);
}
