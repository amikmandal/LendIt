package com.example.sabrinapin.lendit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity  {


    LoginButton loginButton;
    TextView textView;
    CallbackManager callbackManager;
    SharedPreferences sharedPref;
    //user's name

    //userID

    public static String loginDecision = "loginInfo";
    public static String userID;
    public static String USER_FIRST_NAME;
    public static String USER_LAST_NAME;
    public static User mUser;
    int login = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting the shared preferences under the name loginDecision - naming the table
        sharedPref= getSharedPreferences(loginDecision, MODE_PRIVATE);
        Intent receivedIntent = getIntent();

//        if(receivedIntent.getBooleanExtra("clear", false))  {
//            SharedPreferences.Editor editMe = sharedPref.edit();
//            editMe.remove("user");
//            editMe.remove("firstName");
//            editMe.remove("lastName");
//            editMe.apply();
//
//        }


        //if user was already logged in, it will go straight to main
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

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        loginButton = (LoginButton)findViewById(R.id.login_button);

        textView = (TextView)findViewById(R.id.textView);


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                //puts "user" in shared pref to make sure that MainActivity will come up first until logged out
                sharedPref.edit().putString("user", loginResult.getAccessToken().getUserId()).commit();
                //Toast.makeText(LoginActivity.this, loginResult.getAccessToken().getUserId(), Toast.LENGTH_SHORT).show();

                //gets profile
                AccessToken accessToken = loginResult.getAccessToken();

                //saves User ID
                userID = loginResult.getAccessToken().getUserId();

                //When login results are successful it displays the user's name

                final Intent intent = new Intent(LoginActivity.this, MainActivity.class);

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
                                    intent.putExtra("firstname",USER_FIRST_NAME);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                sharedPref.edit().putString("firstName", USER_FIRST_NAME).apply();
                                //Toast.makeText(LoginActivity.this, USER_FIRST_NAME, Toast.LENGTH_SHORT).show();

                                try {
                                    USER_LAST_NAME = object.getString("last_name");
                                    intent.putExtra("lastname", USER_LAST_NAME);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                sharedPref.edit().putString("lastName", USER_LAST_NAME).apply();

                                Log.v("LoginActivity", response.getRawResponse());
                                Log.v("AccessToken", AccessToken.getCurrentAccessToken().getToken());
                                try {
                                    Log.d("FIRST NAME", object.getString("first_name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                intent.putExtra("id", userID);
                            }

                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();

                //made a user object
                mUser = new User(USER_FIRST_NAME, USER_LAST_NAME, userID);

                textView.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );


                //continues to nextActivity, which is MainActivity

                startActivity(intent);

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
        //takes in current info then goes to MainActivity
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        intent.putExtra("firstname", sharedPref.getString("firstName", "Swathi"));
        intent.putExtra("lastname", sharedPref.getString("lastName","Ramprasad"));
        intent.putExtra("id", sharedPref.getString("user", "00000000Liam"));
        startActivity(intent);

        //when you press back arrow it will end
        finish();
    }


}


