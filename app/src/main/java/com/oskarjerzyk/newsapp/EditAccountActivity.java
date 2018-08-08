package com.oskarjerzyk.newsapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class EditAccountActivity extends AppCompatActivity {

    private DatabaseReference database;
    private FirebaseAuth firebaseAuth;

    private Toolbar toolbar;

    private Button saveButton;
    private FloatingActionButton addPhotoButton;
    private EditText forenameEditText;
    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText addressEditText;
    private ImageView photoImageView;

    private PersonalData personalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        database = FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseAuth = FirebaseAuth.getInstance();

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Account");

        saveButton = (Button) findViewById(R.id.save_button_editaccount);
        addPhotoButton = (FloatingActionButton) findViewById(R.id.add_photo_button_editaccount);
        forenameEditText = (EditText) findViewById(R.id.forename_edittext_editaccount);
        nameEditText = (EditText) findViewById(R.id.name_edittext_editaccount);
        phoneEditText = (EditText) findViewById(R.id.phone_edittext_editaccount);
        addressEditText = (EditText) findViewById(R.id.address_edittext_editaccount);
        photoImageView = (ImageView) findViewById(R.id.photo_imageview_editaccount);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserDataInDatabase();
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO get image from gallery then store it in database and update download url
            }
        });

        personalData = new PersonalData();
    }

    @Override
    protected void onStart() {
        super.onStart();

        String UID = firebaseAuth.getUid().toString();
        DatabaseReference hintDatabase = database.child(UID).child("personal-data");

        hintDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                personalData = dataSnapshot.getValue(PersonalData.class);
                forenameEditText.setHint(personalData.getForename());
                nameEditText.setHint(personalData.getName());
                phoneEditText.setHint(personalData.getPhone());
                addressEditText.setHint(personalData.getAddress());
                Picasso.with(EditAccountActivity.this).load(personalData.getImage()).into(photoImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Data provided by user will be stored in database,
     * user do not have to complete all fields,
     * when data is stored, user will be send
     * to AccountActivity
     */
    private void updateUserDataInDatabase() {
        String forename = forenameEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String address = addressEditText.getText().toString();

        String UID = firebaseAuth.getUid().toString();
        DatabaseReference newData = database.child(UID).child("personal-data");

        if (!TextUtils.isEmpty(forename)) {
            newData.child("forename").setValue(forename);
        }
        if (!TextUtils.isEmpty(name)) {
            newData.child("name").setValue(name);
        }
        if (!TextUtils.isEmpty(phone)) {
            newData.child("phone").setValue(phone);
        }
        if (!TextUtils.isEmpty(address)) {
            newData.child("address").setValue(address);
        }

        sendToAccountActivity();
    }

    private void sendToAccountActivity() {
        Intent accountIntent = new Intent(EditAccountActivity.this, AccountActivity.class);
        startActivity(accountIntent);
    }
}
