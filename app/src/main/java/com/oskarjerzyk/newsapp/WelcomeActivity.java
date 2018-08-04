package com.oskarjerzyk.newsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    private Button signInButton;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        signInButton = (Button) findViewById(R.id.signin_welcome_button);
        signUpButton = (Button) findViewById(R.id.signup_welcome_button);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(signInIntent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(WelcomeActivity.this, RegisterActivity.class);
                startActivity(signUpIntent);
            }
        });
    }
}
