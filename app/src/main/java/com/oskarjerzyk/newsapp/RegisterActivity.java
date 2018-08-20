package com.oskarjerzyk.newsapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    //TODO implement email validation

    private FirebaseAuth firebaseAuth;
    private DatabaseReference database;

    private Button registerButton;
    private ImageButton returnButton;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference().child("Users");

        registerButton = (Button) findViewById(R.id.signin_button_register);
        returnButton = (ImageButton) findViewById(R.id.return_button_register);
        emailEditText = (EditText) findViewById(R.id.register_email_edittext);
        passwordEditText = (EditText) findViewById(R.id.register_password_edittext);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewUser();
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent(RegisterActivity.this, WelcomeActivity.class);
                startActivity(returnIntent);
            }
        });
    }

    /**
     * Registration new user with email and password
     * Error message when registration fails
     */
    private void registerNewUser() {
        final String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                addUserToDatabase(email);
                                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registration Error", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(RegisterActivity.this, "Email and Pasword have to be provided!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Storing user in database as Users child
     * user id is UID get from FirebaseAuth
     *
     * @param email - get from EditText in method registerNewUser()
     */
    private void addUserToDatabase(String email) {
        String UID = firebaseAuth.getUid().toString();
        DatabaseReference newUser = database.child(UID).child("personal-data");
        DatabaseReference configured = database.child(UID).child("configured");
        newUser.child("email").setValue(email);
        newUser.child("image").setValue("https://firebasestorage.googleapis.com/v0/b/newsapp-96088.appspot.com/o/Images%2Fblank.jpg?alt=media&token=c66a571d-6c96-43ca-94f2-373840541c5a");
        configured.setValue("false");
    }
}
