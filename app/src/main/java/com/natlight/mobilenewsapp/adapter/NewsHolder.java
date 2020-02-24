package com.natlight.mobilenewsapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView articleTitle;
    ImageView articleImage;

    public NewsHolder(View view) {
        super(view);
        view.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
    }
}
