package com.natlight.mobilenewsapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.natlight.mobilenewsapp.R;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;


public class SourceHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
    ItemClickListener itemClickListener;
    TextView source_title;
    RoundedImageView source_image;

    public SourceHolder(@NonNull View itemView) {
        super(itemView);

        source_image = itemView.findViewById(R.id.source_image);
        source_title  = itemView.findViewById(R.id.source_name);

        itemView.setOnClickListener(this);
    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
