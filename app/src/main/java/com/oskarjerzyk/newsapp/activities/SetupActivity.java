package com.oskarjerzyk.newsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oskarjerzyk.newsapp.R;

public class SetupActivity extends AppCompatActivity {

    private DatabaseReference database;
    private FirebaseAuth firebaseAuth;

    private Toolbar toolbar;

    private Button saveButton;
    private EditText forenameEditText;
    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText addressEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        database = FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseAuth = FirebaseAuth.getInstance();

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Setup Account");

        saveButton = (Button) findViewById(R.id.save_button_setup);
        forenameEditText = (EditText) findViewById(R.id.forename_edittext_setup);
        nameEditText = (EditText) findViewById(R.id.name_edittext_setup);
        phoneEditText = (EditText) findViewById(R.id.phone_edittext_setup);
        addressEditText = (EditText) findViewById(R.id.address_edittext_setup);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeUserDataInDatabase();
            }
        });
    }

    /**
     * All EditTexts have to be completed
     * if not, error message will be shown
     * and hint become red. If all fields completed
     * data will be stored in database and configured
     * value set to true, then user will be send
     * to MainActivity
     */
    private void storeUserDataInDatabase() {
        String forename = forenameEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String address = addressEditText.getText().toString();

        if (!TextUtils.isEmpty(forename) && !TextUtils.isEmpty(name)
                && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(address)) {

            String UID = firebaseAuth.getUid().toString();
            DatabaseReference newData = database.child(UID).child("personal-data");

            newData.child("forename").setValue(forename);
            newData.child("name").setValue(name);
            newData.child("phone").setValue(phone);
            newData.child("address").setValue(address);

            database.child(UID).child("configured").setValue("true");

            sendToMainActivity();
        } else {
            forenameEditText.setHintTextColor(getResources().getColor(R.color.colorError));
            nameEditText.setHintTextColor(getResources().getColor(R.color.colorError));
            phoneEditText.setHintTextColor(getResources().getColor(R.color.colorError));
            addressEditText.setHintTextColor(getResources().getColor(R.color.colorError));
            Toast.makeText(SetupActivity.this, "All fields have to be completed", Toast.LENGTH_LONG).show();
        }
    }

    private void sendToMainActivity() {
        Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
