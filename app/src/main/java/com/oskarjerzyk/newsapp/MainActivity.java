package com.oskarjerzyk.newsapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    Context context;
    ArrayList progList;

    private List<String> progImages;
    private List<String> progHeaders;
    private List<String> progURLs;

    private Spidersweb spidersweb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progHeaders = new ArrayList<String>();
        progImages = new ArrayList<String>();
        progURLs = new ArrayList<String>();

        try {
            spidersweb = new Spidersweb();
        } catch (IOException e) {
            e.printStackTrace(); //@TODO if fails, provide some screen with error message
        }

        try {
            spidersweb.downloadAllURLs();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
//        progHeaders.add("Object");
//        progHeaders.add("Class");
//        progHeaders.add("Object");
//        progHeaders.add("Class");
//        progHeaders.add("Object");
//        progHeaders.add("Class");


//        MyListAdapter adapter = new MyListAdapter(this, progHeaders, progImages);
//        lv = (ListView) findViewById(R.id.listView2);
//        lv.setAdapter(adapter);
    }
}
