package com.oskarjerzyk.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference database;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<String> progImages;
    private List<String> progHeaders;
    private List<String> progURLs;

    //private Spidersweb spidersweb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance().getReference().child("News");

        progHeaders = new ArrayList<String>();
        progImages = new ArrayList<String>();
        progURLs = new ArrayList<String>();

//        try {
//            spidersweb = new Spidersweb();
//        } catch (IOException e) {
//            e.printStackTrace(); //@TODO if fails, provide some screen with error message
//        }
//
//        try {
//            spidersweb.downloadAllURLs();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        recyclerView = (RecyclerView) findViewById(R.id.news_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<News, NewsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, NewsViewHolder>(
                News.class,
                R.layout.news_card,
                NewsViewHolder.class,
                database
        ) {
            @Override
            protected void populateViewHolder(NewsViewHolder viewHolder, News model, int position) {

                viewHolder.setHeader(model.getHeader());
                viewHolder.setImage(getApplicationContext(), model.getImage());

                final String newsURL = model.getUrl();

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openUrlInBrowser(newsURL);
                    }
                });
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void openUrlInBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(url));
        startActivity(browserIntent);
    }
}
