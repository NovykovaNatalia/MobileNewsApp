package com.natlight.mobilenewsapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.natlight.mobilenewsapp.Model.Post;
import com.natlight.mobilenewsapp.services.NetworkService;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView userId;
    private TextView id;
    private TextView title;
    private TextView body;
    private Button button;
    private Button nxtAct;
    RecyclerView listWebSite;
    RecyclerView layoutMeneger;
    SwipeRefreshLayout refreshLayout;
    public  static final String API_KEY="017ac0ad7b2c4367ba9e0a08343d8e9b";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userId = findViewById(R.id.userId);
        id = findViewById(R.id.id);
        title = findViewById(R.id.title);
        body = findViewById(R.id.body);
        button = findViewById(R.id.button);
        nxtAct = findViewById(R.id.nextActivity);

        Intent intent = new Intent(this, SecondActivity.class);
        nxtAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetworkService.getInstance()        //Return Reference on NetworkService obj.
                        .getJSONApi()               //Return JSonPlaceHolderAPI from metworkService obj;
                        .getPostWithID(1)           //Call method from JSONPlaceHolderAPI it return ass CAll<Post> wrapper obj; obj,enque
                        .enqueue(new Callback<Post>() {     //ADD anonymus class from Callback interface, which will process responce.
                            @Override
                            public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                                Post post = response.body();
                                assert post != null;
                                id.append("Id: " + post.getId() + "\n");
                                userId.append("UserId: " + post.getUserId() + "\n");
                                title.append("Title: " + post.getTitle() + "\n");
                                body.append("Body: " + post.getBody() + "\n");
                            }

                            @Override
                            public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {
                                id.append("Error occured while getting request!");
                                t.printStackTrace();
                            }
                        });
            }
        });

    }
}
