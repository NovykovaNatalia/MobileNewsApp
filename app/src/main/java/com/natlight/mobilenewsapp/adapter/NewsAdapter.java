package com.natlight.mobilenewsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.natlight.mobilenewsapp.ArticleActivity;
import com.natlight.mobilenewsapp.Model.Article;
import com.natlight.mobilenewsapp.R;
import com.natlight.mobilenewsapp.utils.ISO8601Parse;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsHolder> {
    public NewsAdapter(List<Article> articleList, Context context, int defaultImageId) {
        this.articleList = articleList;
        this.context = context;
        this.defaultImageId = defaultImageId;
    }

    private int defaultImageId;
    private List<Article> articleList;
    private Context context;

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.news_layout, parent, false);
        return new NewsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
        String imgUrl = articleList.get(position).getUrlToImage();
        if (imgUrl != null && !imgUrl.isEmpty()) {
            try {
                Picasso.get()
                        .load(imgUrl)
                        .into(holder.articleImage);
            } catch (Throwable e) {
                Log.e("mobileNewsApp", String.format("NewsAdapter: Picasso can't set image. imageURL: %s exception cause: %s ",
                        imgUrl, e.getCause()));
            }
        } else {
            holder.articleImage.setImageResource(defaultImageId);
        }

        if (articleList.get(position).getTitle().length() > 65)
            holder.articleTitle.setText(articleList.get(position).getTitle().substring(0, 65) + "...");
        else
            holder.articleTitle.setText(articleList.get(position).getTitle());
        if (articleList.get(position).getPublishedAt() != null && !articleList.get(position).getPublishedAt().isEmpty() ) {
            Date date = null;
            try {
                date = ISO8601Parse.parse(articleList.get(position).getPublishedAt());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date != null) {
                holder.articleTime.setReferenceTime(date.getTime());
            }
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent detail = new Intent(context, ArticleActivity.class);
                detail.putExtra("webURL", articleList.get(position).getUrl());
                detail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(detail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
}
