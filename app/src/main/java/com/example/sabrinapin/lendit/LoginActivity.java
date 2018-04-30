package com.example.sabrinapin.lendit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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

        //initializes sharedPrefrences
        //we use sharedPreferences to determine what activity the user is in
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        //If a user exits the app while in UsernameActivity, the app will return to that activity
        if(sharedPref.contains("inUsernameActivity") && sharedPref.contains("user")){
            Intent intent = new Intent(this, UsernameActivity.class);
            this.startActivity(intent);
            finish();
        }

        //If a user exits the app while in NewTransaction, the app will return to that transaction
        if(sharedPref.contains("inTransaction") && sharedPref.contains("user")){
           // Toast.makeText(this, "This went to LoginActivity, but it contains user and inTransaction. Should go to NewTransaction", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, NewTransaction.class);
            this.startActivity(intent);
            finish();
        }


        //If a user exits the app while in Calendar, the app will return to that activity
        if(sharedPref.contains("inCalendar") && sharedPref.contains("user")){
            Intent intent = new Intent(this, CaldroidSampleActivity.class);
            this.startActivity(intent);
            finish();
        }

        //If user exits app, but was already logged in, it will go straight to MainActivity
        if(sharedPref.contains("user")) {
            Log.d("user---------", sharedPref.getString("user", null));
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("username", sharedPref.getString("user", null));
            Toast.makeText(this, "Passing through intent", Toast.LENGTH_SHORT).show();
            intent.putExtra("firstname", sharedPref.getString("firstName", "Amik"));
            intent.putExtra("lastname", sharedPref.getString("lastName","Mandal"));
            intent.putExtra("id", sharedPref.getString("user", "00000000Liam"));
            this.startActivity(intent);
            finish();
        }


        //sets up Facebook API
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        //creates Facebook loginButton and textView
        loginButton = (LoginButton)findViewById(R.id.login_button);
        textView = (TextView)findViewById(R.id.textView);


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                mUser = new User();

                //puts "user" in shared pref to make sure that MainActivity will come up first until logged out
                sharedPref.edit().putString("user", loginResult.getAccessToken().getUserId()).commit();
                //Toast.makeText(LoginActivity.this, loginResult.getAccessToken().getUserId(), Toast.LENGTH_SHORT).show();

                //gets profile accessToken
                AccessToken accessToken = loginResult.getAccessToken();

                //saves User ID
                userID = loginResult.getAccessToken().getUserId();

                //From stackoverflow - stores user's first and last name using JSON object
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

                                //THIS METHOD IS ASYNCHRONOUS
                                //checks usernames
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

                //Creates/initializes user object
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
                //When user cancels attempt it returns back to login screen
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


    //This is only called when login is successful
    private void nextActivity(){

        //Goes to UsernameActivity
        Intent intent = new Intent(LoginActivity.this, UsernameActivity.class);
        intent.putExtra("firstname", sharedPref.getString("firstName", "Swathi"));
        intent.putExtra("lastname", sharedPref.getString("lastName","Ramprasad"));
        intent.putExtra("id", sharedPref.getString("user", "00000000Liam"));
        startActivity(intent);

        //When you press back arrow it will end
        finish();
    }


}


