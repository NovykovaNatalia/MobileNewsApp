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
import android.widget.Filter;
import android.widget.Filterable;

import com.natlight.mobilenewsapp.Model.NewsSources;
import com.natlight.mobilenewsapp.Model.Source;
import com.natlight.mobilenewsapp.NewsActivity;
import com.natlight.mobilenewsapp.R;
import com.natlight.mobilenewsapp.utils.ImageUtils;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SourceAdapter extends RecyclerView.Adapter<SourceHolder> implements Filterable {
    private Context context;
    private NewsSources newsSources;
    private List<Source> filteredSources;

    public SourceAdapter(Context context, NewsSources newsSources) {
        this.context = context;
        this.newsSources = newsSources;
        filteredSources = new ArrayList<>(newsSources.getSources());
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
        sourceHolder.source_title.setText(filteredSources.get(position).getName());
        String sourceImageName = ImageUtils.getImageName(filteredSources.get(position).getName());
        int id = context.getResources().getIdentifier(sourceImageName, "drawable", context.getPackageName());

        if (id != 0) {
            sourceHolder.source_image.setImageResource(id);
        }

        sourceHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(context, NewsActivity.class);
                intent.putExtra("source", filteredSources.get(position).getId());
                intent.putExtra("imageId", id);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredSources.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Source> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(newsSources.getSources());
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Source source : newsSources.getSources()) {
                    if (source.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(source);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredSources.clear();
            filteredSources.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}