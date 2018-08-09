package com.oskarjerzyk.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class NewsViewHolder extends RecyclerView.ViewHolder {

    public View view;
    public ImageButton bookmarkButton;
    public ImageButton favouriteButton;

    public DatabaseReference readLaterDatabase;
    public DatabaseReference favouriteDatabase;
    public FirebaseAuth firebaseAuth;

    public NewsViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        bookmarkButton = (ImageButton) view.findViewById(R.id.news_bookmark);
        favouriteButton = (ImageButton) view.findViewById(R.id.news_favourite);

        readLaterDatabase = FirebaseDatabase.getInstance().getReference().child("Read-Later");
        favouriteDatabase = FirebaseDatabase.getInstance().getReference().child("Favourites");
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void setHeader(String header) {
        TextView newsHeader = (TextView) view.findViewById(R.id.news_header);
        newsHeader.setText(header);
    }

    public void setImage(Context context, String image) {
        ImageView newsImage = (ImageView) view.findViewById(R.id.news_image);
        Picasso.with(context).load(image).into(newsImage);
    }

    public void setBookmarkButtonIcon(final String newsKey) {

        readLaterDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(newsKey).hasChild(firebaseAuth.getCurrentUser().getUid())) {
                    bookmarkButton.setImageResource(R.drawable.bookmark_filled);
                } else {
                    bookmarkButton.setImageResource(R.drawable.bookmark_icon_white);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setFavouriteButtonIcon(final String newsKey) {

        favouriteDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(newsKey).hasChild(firebaseAuth.getCurrentUser().getUid())) {
                    favouriteButton.setImageResource(R.drawable.favourite_filled);
                } else {
                    favouriteButton.setImageResource(R.drawable.favourite_icon_white);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}