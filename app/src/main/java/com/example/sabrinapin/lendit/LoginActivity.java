package com.example.sabrinapin.lendit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.Login;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity  {


    LoginButton loginButton;
    TextView textView;
    CallbackManager callbackManager;
    SharedPreferences sharedPref;

    public static String loginDecision = "loginInfo";
    public static String userID;
    public static String USER_FIRST_NAME;
    public static String USER_LAST_NAME;
    public static String USERNAME;
    public static User mUser;
    private FirebaseDatabase mRoot;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRoot = FirebaseDatabase.getInstance("https://lendit-af1e0.firebaseio.com/");
        userRef = mRoot.getReference().child("users");

        //for rotation
        //determine rotation
       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        //NEW WAY HOW SHAREDPREFERENCES ARE STORED
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        //removed inTransaction
        //sharedPref.edit().remove("inTransaction").commit();


        // try making a sharedPref that changes when you go to transaction adapter - and is removed when you complete transaction
        if(sharedPref.contains("inTransaction") && sharedPref.contains("user")){
            Toast.makeText(this, "This went to LoginActivity, but it contains user and inTransaction. Should go to NewTransaction", Toast.LENGTH_LONG).show();
            //make it continue on to NewTransaction
            //BUT MAKE SURE THE SHIT YOU TYPED IN STAYS THERE
            Intent intent = new Intent(this, NewTransaction.class);
            this.startActivity(intent);
            finish();
        }

        //testing to determine if it it should be inTransaction
        if(!sharedPref.contains("inTransaction")){
            Toast.makeText(this, "Does NOT contain inTransaction", Toast.LENGTH_SHORT).show();
        }

        //if user was already logged in, it will go straight to main
        if(sharedPref.contains("user")) {
            Log.d("user---------", sharedPref.getString("user", null));
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("username", sharedPref.getString("user", null));
            Toast.makeText(this, "User was already logged in", Toast.LENGTH_SHORT).show();
            intent.putExtra("firstname", sharedPref.getString("firstName", "Amik"));
            intent.putExtra("lastname", sharedPref.getString("lastName","Mandal"));
            intent.putExtra("id", sharedPref.getString("user", "00000000Liam"));
            this.startActivity(intent);
            finish();
        }
        //fix here

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        loginButton = (LoginButton)findViewById(R.id.login_button);

        textView = (TextView)findViewById(R.id.textView);

        //Toast.makeText(this, "Passing through intent", Toast.LENGTH_SHORT).show();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                mUser = new User();

                //puts "user" in shared pref to make sure that MainActivity will come up first until logged out
                sharedPref.edit().putString("user", loginResult.getAccessToken().getUserId()).commit();
                //Toast.makeText(LoginActivity.this, loginResult.getAccessToken().getUserId(), Toast.LENGTH_SHORT).show();

                //gets profile
                AccessToken accessToken = loginResult.getAccessToken();

                //saves User ID
                userID = loginResult.getAccessToken().getUserId();

                //Testing from stackOverflow
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

                                try {
                                    USER_FIRST_NAME = object.getString("first_name");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                sharedPref.edit().putString("firstName", USER_FIRST_NAME).commit();
                                Log.d("firstName", USER_FIRST_NAME);
                                //Toast.makeText(LoginActivity.this, USER_FIRST_NAME, Toast.LENGTH_SHORT).show();

                                try {
                                    USER_LAST_NAME = object.getString("last_name");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                sharedPref.edit().putString("lastName", USER_LAST_NAME).commit();
                                Log.d("firstName", USER_LAST_NAME);

                                Log.v("LoginActivity", response.getRawResponse());
                                Log.v("AccessToken", AccessToken.getCurrentAccessToken().getToken());
                                try {
                                    Log.d("FIRST NAME", object.getString("first_name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //continues to nextActivity, which is MainActivity
                                //NEXT ACTIVITY IS MOVED HERE BECAUSE THIS METHOD IS ASYNCHRONOUS
                                DatabaseReference myUsernames = mRoot.getReference("usernames");

                                myUsernames.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        if (snapshot.hasChild(userID)) {
                                            Intent curintent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(curintent);
                                            finish();

                                        }
                                        else{
                                            nextActivity();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();

                //made a user object
                mUser.setUSER_ID(userID);
                mUser.setUSER_FIRST(USER_FIRST_NAME);
                mUser.setUSER_LAST(USER_LAST_NAME);

                textView.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );


            }

            @Override
            public void onCancel() {
                //When user cancels attempt
                textView.setText("Attempt is cancelled");
            }

            @Override
            public void onError(FacebookException exception) {
                // Exits login with error
                textView.setText("Failure");
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void nextActivity(){
        //only called when login is Successful

        //if true then skip
        //if(else) sharedpreference yields a certain thing have it continue to username activity

        //takes in current info then goes to MainActivity
        Intent intent = new Intent(LoginActivity.this, UsernameActivity.class);
        //eventually put it so that it goes onto UsernameActivity.class


        intent.putExtra("firstname", sharedPref.getString("firstName", "Swathi"));
        intent.putExtra("lastname", sharedPref.getString("lastName","Ramprasad"));
        intent.putExtra("id", sharedPref.getString("user", "00000000Liam"));
        startActivity(intent);

        //when you press back arrow it will end
        finish();
    }


}


