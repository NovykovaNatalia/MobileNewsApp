package com.natlight.mobilenewsapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.natlight.mobilenewsapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView articleTitle;
    CircleImageView articleImage;
    RelativeTimeTextView articleTime;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    ItemClickListener itemClickListener;

    public NewsHolder(View view) {
        super(view);
        articleTitle = (TextView)itemView.findViewById(R.id.article_title);
        articleImage = (CircleImageView)itemView.findViewById(R.id.article_image);
        articleTime = (RelativeTimeTextView)itemView.findViewById(R.id.article_time);

        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(),false);
    }
}
