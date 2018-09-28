package com.oskarjerzyk.newsapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oskarjerzyk.newsapp.R;
import com.oskarjerzyk.newsapp.adapter.FavouritesReadLaterAdapter;
import com.oskarjerzyk.newsapp.model.News;

import java.util.ArrayList;
import java.util.List;

public class ReadLaterActivity extends AppCompatActivity {

    private DatabaseReference databaseUsers;
    private FirebaseAuth firebaseAuth;

    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private FavouritesReadLaterAdapter adapter;
    private LinearLayoutManager layoutManager;

    final List<News> listOfReadLater = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_later);

        databaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseAuth = FirebaseAuth.getInstance();

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Read Later");

        layoutManager = new LinearLayoutManager(ReadLaterActivity.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        adapter = new FavouritesReadLaterAdapter(this, listOfReadLater);

        recyclerView = (RecyclerView) findViewById(R.id.read_later_news_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Retrieving read later newses data
     * and storing it in ArrayList.
     * This data will be displayed in RecyclerView
     */
    @Override
    protected void onStart() {
        super.onStart();

        String UID = firebaseAuth.getUid().toString();
        DatabaseReference databaseFavourites = databaseUsers.child(UID).child("read-later");
        final DatabaseReference databaseNews = FirebaseDatabase.getInstance().getReference().child("News");

        databaseFavourites.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String currentKey = snapshot.getKey();
                    DatabaseReference currentNews = databaseNews.child(currentKey);

                    final News news = new News("", "", "");

                    currentNews.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String header = dataSnapshot.child("header").getValue(String.class).toString();
                            String image = dataSnapshot.child("image").getValue(String.class).toString();
                            String url = dataSnapshot.child("url").getValue(String.class).toString();

                            news.setHeader(header);
                            news.setImage(image);
                            news.setUrl(url);

                            listOfReadLater.add(news);
                            adapter.notifyDataSetChanged();
                            layoutManager.scrollToPositionWithOffset(listOfReadLater.size() - 1, 0);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * List need to be cleared in case to prevent
     * from duplicating items in RecyclerView
     * after coming back from browser
     */
    @Override
    protected void onResume() {
        super.onResume();
        listOfReadLater.clear();
    }

    /**
     * Scrolling to the top, then returning
     * to MainActivity
     */
    @Override
    public void onBackPressed() {
        if (layoutManager.findFirstCompletelyVisibleItemPosition() == listOfReadLater.size() - 1
                || layoutManager.findFirstCompletelyVisibleItemPosition() == listOfReadLater.size() - 2) {
            super.onBackPressed();
        } else {
            recyclerView.smoothScrollToPosition(listOfReadLater.size() - 1);
        }
    }
}
