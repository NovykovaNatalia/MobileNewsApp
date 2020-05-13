package com.natlight.mobilenewsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.natlight.mobilenewsapp.Model.NewsSources;
import com.natlight.mobilenewsapp.NewsActivity;
import com.natlight.mobilenewsapp.R;
import com.natlight.mobilenewsapp.utils.ImageUtils;

import java.io.InputStream;
import java.net.URL;

public class SourceAdapter extends RecyclerView.Adapter<SourceHolder> {
    private Context context;
    private NewsSources newsSources;

    public SourceAdapter(Context context, NewsSources newsSources) {
        this.context = context;
        this.newsSources = newsSources;
    }

    @NonNull
    @Override
    public SourceHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.source_layout, parent, false);
        return new SourceHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SourceHolder sourceHolder, int position) {
        sourceHolder.source_title.setText(newsSources.getSources().get(position).getName());
        String sourceImageName = ImageUtils.getImageName(newsSources.getSources().get(position).getName());
        int id = context.getResources().getIdentifier(sourceImageName, "drawable", context.getPackageName());

        if (id != 0) {
            sourceHolder.source_image.setImageResource(id);
        }

        sourceHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(context, NewsActivity.class);
                intent.putExtra("source", newsSources.getSources().get(position).getId());
                intent.putExtra("imageId", id);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsSources.getSources().size();
    }
}