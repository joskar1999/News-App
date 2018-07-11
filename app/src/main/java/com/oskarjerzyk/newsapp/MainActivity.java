package com.oskarjerzyk.newsapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    Context context;
    ArrayList progList;

    public List<Integer> progImages;

    public List<String> progNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progNames = new ArrayList<String>();
        progImages = new ArrayList<Integer>();

        /*progImages.add(R.drawable.ikona);
        progImages.add(R.drawable.zd);
        progImages.add(R.drawable.ikona);
        progImages.add(R.drawable.zd);
        progImages.add(R.drawable.ikona);
        progImages.add(R.drawable.zd);*/


        progNames.add("Object");
        progNames.add("Class");
        progNames.add("Object");
        progNames.add("Class");
        progNames.add("Object");
        progNames.add("Class");


        MyListAdapter adapter = new MyListAdapter(this,progNames,progImages);

        lv = (ListView) findViewById(R.id.listView2);

        lv.setAdapter(adapter);
    }
}
