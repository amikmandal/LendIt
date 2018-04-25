package com.example.sabrinapin.lendit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static com.example.sabrinapin.lendit.LoginActivity.USERNAME;
import static com.example.sabrinapin.lendit.LoginActivity.USER_LAST_NAME;
import static com.example.sabrinapin.lendit.LoginActivity.loginDecision;
import static com.example.sabrinapin.lendit.LoginActivity.mUser;

public class UsernameActivity extends AppCompatActivity {

    EditText usernameView;
    TextView welcomeView;
    private String usernameInput;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        welcomeView = findViewById(R.id.welcomeMessage);
        usernameView = findViewById(R.id.usernameInput);


        String userId = mUser.getUSER_ID();
        if(true){ Intent intent = new Intent(UsernameActivity.this, MainActivity.class);}

        USERNAME = usernameView.getText().toString();
        mUser.setUSERNAME(USERNAME);
        sharedPref= getSharedPreferences(loginDecision, MODE_PRIVATE);
        sharedPref.edit().putString("userName",USERNAME).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
