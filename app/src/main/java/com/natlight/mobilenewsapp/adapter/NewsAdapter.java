package com.natlight.mobilenewsapp.adapter;

import android.content.Context;

import com.natlight.mobilenewsapp.Model.Article;

import java.util.List;

public class NewsAdapter {
    public NewsAdapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    private List<Article>articleList;
    private Context context;
}
