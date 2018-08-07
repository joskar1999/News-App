package com.oskarjerzyk.newsapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {

    private DatabaseReference database;
    private FirebaseAuth firebaseAuth;

    private Toolbar toolbar;
    private Button editProfileButton;
    private TextView emailTextView;
    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView addressTextView;
    private CircleImageView profileImageView;

    private PersonalData personalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        database = FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseAuth = FirebaseAuth.getInstance();

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Account");

        personalData = new PersonalData();

        editProfileButton = (Button) findViewById(R.id.edit_profile_button_account);
        emailTextView = (TextView) findViewById(R.id.email_textview_account);
        nameTextView = (TextView) findViewById(R.id.name_textview_account);
        phoneTextView = (TextView) findViewById(R.id.phone_textview_account);
        addressTextView = (TextView) findViewById(R.id.address_textview_account);
        profileImageView = (CircleImageView) findViewById(R.id.person_imageview_account);

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editProfileIntent = new Intent(AccountActivity.this, EditAccountActivity.class);
                startActivity(editProfileIntent);
            }
        });
    }

    /**
     * Personal data will be downloaded
     * from Firebase and set to appropriate
     * TextViews
     */
    @Override
    protected void onStart() {
        super.onStart();

        String UID = firebaseAuth.getUid().toString();
        DatabaseReference personalDataDatabase = database.child(UID).child("personal-data");

        personalDataDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                personalData = dataSnapshot.getValue(PersonalData.class);

                emailTextView.setText(personalData.getEmail());
                nameTextView.setText(personalData.getForename() + " " + personalData.getName());
                phoneTextView.setText(personalData.getPhone());
                addressTextView.setText(personalData.getAddress());
                Picasso.with(AccountActivity.this).load(personalData.getImage()).into(profileImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
