package com.oskarjerzyk.newsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference database;
    private FirebaseAuth firebaseAuth;

    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private ProgressDialog progressDialog;

    private List<String> progImages;
    private List<String> progHeaders;
    private List<String> progURLs;

    private Spidersweb spidersweb;
    private Forbes forbes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance().getReference().child("News");
        firebaseAuth = FirebaseAuth.getInstance();

        progHeaders = new ArrayList<String>();
        progImages = new ArrayList<String>();
        progURLs = new ArrayList<String>();

        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView = (RecyclerView) findViewById(R.id.news_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.sidebar_navigation);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Setting up RecyclerView, displaying newses
     * downloaded from Firebase
     */
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser == null) {
            sendToWelcomeActivity();
        }

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

    private void sendToWelcomeActivity() {
        Intent welcomeIntent = new Intent(MainActivity.this, WelcomeActivity.class);
        startActivity(welcomeIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Menu item events handling
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_refresh) {
            new RefreshNews().execute();
        } else if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Sidebar events handling
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sidebar_account: {
                break;
            }
            case R.id.sidebar_favourites: {
                break;
            }
            case R.id.sidebar_read_later: {
                break;
            }
            case R.id.sidebar_settings: {
                break;
            }
            case R.id.sidebar_logout: {
                logout();
                break;
            }
        }

        return false;
    }

    /**
     * Log out and send user to WelcomeActivity
     */
    private void logout() {
        firebaseAuth.signOut();
        Intent welcomeIntent = new Intent(MainActivity.this, WelcomeActivity.class);
        startActivity(welcomeIntent);
        finish();
    }

    /**
     * This class responsibility is to download
     * tech newses and if necessary to store it
     * in the Firebase
     */
    private class RefreshNews extends AsyncTask<Void, Void, Void> {

        /**
         * When refresh icon is clicked, this method
         * will display ProgressDialog with proper message
         */
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("News App");
            progressDialog.setMessage("News loading...");
            progressDialog.show();
        }

        /**
         * Calling functions which will parse HTML
         * and download URLs, then data is storing
         * in ArrayList and finally send to Firebase
         */
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                spidersweb = new Spidersweb();
                spidersweb.downloadAllURLs();
                progHeaders = spidersweb.getHeadersList();
                progImages = spidersweb.getImageURLList();
                progURLs = spidersweb.getLinks();
                sendDataToFirebase();
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, "Spidersweb Error", Toast.LENGTH_LONG).show();
            }

            try {
                forbes = new Forbes();
                forbes.downloadAllURLs();
                progHeaders = forbes.getHeadersList();
                progImages = forbes.getImageURLList();
                progURLs = forbes.getLinks();
                sendDataToFirebase();
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, "Forbes Error", Toast.LENGTH_LONG).show();
            }

            return null;
        }

        /**
         * After executing all functions
         * ProgressDialog will dismiss
         */
        @Override
        protected void onPostExecute(Void aVoid) {

            progressDialog.dismiss();
        }
    }

    /**
     * Opening website in browser with
     * implicit Intent
     *
     * @param url - website url which will be open
     */
    private void openUrlInBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(url));
        startActivity(browserIntent);
    }

    /**
     * This function will send data stored in ArrayList
     * to Firebase without duplicating data already
     * existed in database
     */
    private void sendDataToFirebase() {

        for (int i = 0; i < 10; i++) {

            final int j = i;
            Query query = database.orderByChild("header").equalTo(progHeaders.get(i));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        DatabaseReference newNews = database.push();
                        newNews.child("header").setValue(progHeaders.get(j));
                        newNews.child("image").setValue(progImages.get(j));
                        newNews.child("url").setValue(progURLs.get(j));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
}
