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

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    private Button registerButton;
    private ImageButton returnButton;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

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

    private void registerNewUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
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
}
