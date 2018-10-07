package com.oskarjerzyk.newsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.oskarjerzyk.newsapp.R;
import com.oskarjerzyk.newsapp.model.News;

import java.util.List;

public class FavouritesReadLaterAdapter extends RecyclerView.Adapter<FavouritesReadLaterAdapter.FavouritesReadLaterViewHolder> {

    //TODO handle favourites and read later icon clicks

    private Context context;
    private List<News> newsList;

    public FavouritesReadLaterAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public FavouritesReadLaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.news_card_no_icons, parent, false);
        return new FavouritesReadLaterViewHolder(itemView);
    }

    /**
     * Setting image and header, assigning url to
     * onClick in ImageView
     */
    @Override
    public void onBindViewHolder(@NonNull final FavouritesReadLaterViewHolder holder, int position) {
        final News news = newsList.get(position);
        holder.headerTextView.setText(news.getHeader());
        Glide.with(context).load(news.getImage()).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getUrl()));
                context.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class FavouritesReadLaterViewHolder extends RecyclerView.ViewHolder {

        public TextView headerTextView;
        public ImageView imageView;

        public FavouritesReadLaterViewHolder(View itemView) {
            super(itemView);
            headerTextView = (TextView) itemView.findViewById(R.id.news_header);
            imageView = (ImageView) itemView.findViewById(R.id.news_image);
        }
    }
}
