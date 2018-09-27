package com.oskarjerzyk.newsapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class FavouritesViewHolder extends RecyclerView.ViewHolder {

    private View view;

    public FavouritesViewHolder(View itemView) {
        super(itemView);
        view = itemView;
    }

    public void setHeader(String header) {
        TextView newsHeader = (TextView) view.findViewById(R.id.news_header);
        newsHeader.setText(header);
    }

    public void setImage(Context context, String image) {
        ImageView newsImage = (ImageView) view.findViewById(R.id.news_image);
        Picasso.with(context).load(image).into(newsImage);
    }
}
