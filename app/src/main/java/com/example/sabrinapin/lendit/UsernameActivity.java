package com.example.sabrinapin.lendit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.sabrinapin.lendit.LoginActivity.USERNAME;
import static com.example.sabrinapin.lendit.LoginActivity.USER_FIRST_NAME;
import static com.example.sabrinapin.lendit.LoginActivity.USER_LAST_NAME;
import static com.example.sabrinapin.lendit.LoginActivity.loginDecision;
import static com.example.sabrinapin.lendit.LoginActivity.mUser;

public class UsernameActivity extends AppCompatActivity {

    EditText usernameView;
    TextView welcomeView;
    private String usernameInput;
    SharedPreferences sharedPref;
    private FirebaseDatabase mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        welcomeView = findViewById(R.id.welcomeMessage);
        usernameView = findViewById(R.id.usernameInput);

        mReference = FirebaseDatabase.getInstance("https://lendit-af1e0.firebaseio.com/");
        DatabaseReference checkNames = mReference.getInstance().getReference().child("checkNames");

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);







        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = mUser.getUSER_ID();
                if(true){ Intent intent = new Intent(UsernameActivity.this, MainActivity.class);}
//                DatabaseReference myRef = mReference.getReference("users");

                DatabaseReference myUsernames = mReference.getReference("checkNames");
                Log.d("nameRef", usernameView.getText().toString());


                myUsernames.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (! snapshot.hasChild(usernameView.getText().toString())) {
                            DatabaseReference tryUsernames = mReference.getReference("users");
                            DatabaseReference checkUsernames = mReference.getReference("checkNames");
                            DatabaseReference putUsernames = mReference.getReference("usernames");



                            USERNAME = usernameView.getText().toString();
                            mUser.setUSERNAME(USERNAME);
                            sharedPref.edit().putString("userName", USERNAME).commit();
                            USER_FIRST_NAME = sharedPref.getString("firstName", "dumb");
                            USER_LAST_NAME = sharedPref.getString("lastName", "dumber");
                            UsernameInformation infoA = new UsernameInformation();
                            infoA.setlenditUsername(USERNAME);
                            infoA.setname(USER_FIRST_NAME + " " + USER_LAST_NAME);

                            putUsernames.child(mUser.getUSER_ID()).setValue(infoA);
                            checkUsernames.child(USERNAME).setValue("exists");

                            Intent intent = new Intent(UsernameActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(UsernameActivity.this,"This username is taken, choose another", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {


                    }
                });



//                myUsernames.child(userId).child("lenditUsername").setValue(USERNAME);
//                myUsernames.child(userId).child("Name").setValue(USER_FIRST_NAME + " " + USER_LAST_NAME);
                    //myRef.child(userId).child("username").setValue(USERNAME);








            }
        });
    }

}
