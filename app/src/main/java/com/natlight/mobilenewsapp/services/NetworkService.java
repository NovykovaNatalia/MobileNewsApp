package com.natlight.mobilenewsapp.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static  NetworkService mInstance;
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final String BASE_NEWS_URL = "https://newsapi.org/";
    private Retrofit mRetrofit;

    private NetworkService(){

    }
    public static NetworkService getInstance(){
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }
    public JSONPlaceHolderApi getJSONApi(){
        initRetrofit(BASE_URL);
        return mRetrofit.create(JSONPlaceHolderApi.class);
    }
    public NewsApi getNewsJSONApi(){
        initRetrofit(BASE_NEWS_URL);
       return mRetrofit.create(NewsApi.class);
    }
    private void initRetrofit (String baseUrl) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public  static  String getAPIUrl(String source, String apiKEY){
        StringBuilder apiUrl = new StringBuilder("https://newsapi.org/v2/top-headlines?sources=");
        return apiUrl.append(source)
                .append("&apiKey=")
                .append(apiKEY)
                .toString();
    }


}
