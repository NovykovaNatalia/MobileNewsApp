package com.natlight.mobilenewsapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.natlight.mobilenewsapp.Model.Post;
import com.natlight.mobilenewsapp.Model.WebSite;
import com.natlight.mobilenewsapp.services.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondActivity extends AppCompatActivity {
    private Button prevButton;
    private Button btnNews;
    private TextView textViewSecondActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);

        prevButton = findViewById(R.id.btnBack);
        btnNews = findViewById(R.id.btnnewsApi);
        textViewSecondActivity = findViewById(R.id.textViewSecondActivity);
        Intent intent = new Intent(this,MainActivity.class);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent);
            }
        });

        btnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetworkService.getInstance()        //Return Reference on NetworkService obj.
                        .getNewsJSONApi()               //Return JSonPlaceHolderAPI from metworkService obj;
                        .getSources()           //Call method from JSONPlaceHolderAPI it return ass CAll<Post> wrapper obj; obj,enque
                        .enqueue(new Callback<WebSite>() {     //ADD anonymus class from Callback interface, which will process responce.
                            @Override
                            public void onResponse(@NonNull Call<WebSite> call, @NonNull Response<WebSite> response) {
                                assert response.body() != null;
                                textViewSecondActivity.setText(response.body().getSources().get(0).getDescription());
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onFailure(@NonNull Call<WebSite> call, @NonNull Throwable t) {
                               textViewSecondActivity.setText("Error occured while getting request!");
                                t.printStackTrace();
                            }
                        });

            }
        });



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
