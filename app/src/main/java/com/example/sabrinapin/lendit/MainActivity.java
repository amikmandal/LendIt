package com.example.sabrinapin.lendit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.sabrinapin.lendit.LoginActivity.USER_FIRST_NAME;
import static com.example.sabrinapin.lendit.LoginActivity.USER_LAST_NAME;

public class MainActivity extends AppCompatActivity {


    private static final int GET_TRANSACTION = 0;
    private ArrayList<Transaction> transactionList;
    private TextView mTextMessage;
    //private TextView trial;
    private Button logout;
    private static final String TAG = "MainActivity";
    private TransactionAdapter mAdapter;

    String[] mPeople;
    String[] mObjects;
    String[] mDates;
    Uri[] mUris;


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private DatabaseReference mtransRef;
    private String userID;
    private String mUsername;
    private ArrayList <String> gotUsername = new ArrayList<String>();


    private DatabaseReference DBR;
    private FirebaseDatabase FDB;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_new_transaction:
                    Intent thisIntent = new Intent(MainActivity.this, NewTransaction.class);
                    startActivityForResult(thisIntent, GET_TRANSACTION);
                    return true;
                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
                    Intent thatIntent = new Intent(MainActivity.this, CaldroidSampleActivity.class);
                    startActivity(thatIntent);
                    return true;
            }
            return false;
        }
    };
    private SharedPreferences sharedPref;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //trial = findViewById(R.id.write);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://lendit-af1e0.firebaseio.com/");
        //TAKING SHARED PREF BACK
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Intent intent = getIntent();
        USER_FIRST_NAME = sharedPref.getString("firstName", "dumb");
        USER_LAST_NAME = sharedPref.getString("lastName", "dumber");
        userID = sharedPref.getString("user", "dumbest");

        FDB = FirebaseDatabase.getInstance();

        myRef = mFirebaseDatabase.getReference().child("usernames").child(userID);

//        //setup using preference manager

        if(sharedPref.contains("inTransaction")){
            intent = new Intent(this, NewTransaction.class);
            this.startActivity(intent);
            Toast.makeText(this, "inTransaction is in sharedPref: FIX THIS", Toast.LENGTH_LONG).show();
            finish();
        }

        if(!sharedPref.contains("inTransaction")){
            Toast.makeText(this, "inTransaction is NOT in sharedPref", Toast.LENGTH_LONG).show();
        }








        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

// sets up the firebase listener
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //this is called once wwith the initial value and again when this is updated
//            showData(dataSnapshot);
                Log.d("firebase", dataSnapshot.toString());

                UsernameInformation mInfo = dataSnapshot.getValue(UsernameInformation.class);
                Log.d("minfo", mInfo.getname());

                Log.d("username", mInfo.getlenditUsername());
                String myUsername = mInfo.getlenditUsername();
                //Log.d("instance", mUsername);


                // put the username in Shared preferences, so that we can access it through the other activity
                mtransRef = mFirebaseDatabase.getReference().child("users").child(myUsername);

                // catch an exception if the user doesn't exist in the database: leave the array as an empty one if the data does not exist


                mtransRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                       int length = (int) dataSnapshot.getChildrenCount();
                // adds the newest transactions first to the arraylist
                        ArrayList<TransFirInfo> myArr = new ArrayList<TransFirInfo>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            TransFirInfo user = snapshot.getValue(TransFirInfo.class);
                            myArr.add(0, user);
                            Log.d("borrower", user.getitem());


                        }
                        mAdapter = new TransactionAdapter(getApplicationContext(), myArr);
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        mRecyclerView.setAdapter(new TransactionAdapter(getApplicationContext(), myArr));
                        //Log.d("arrayCheck", myArr.get(0).toString());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });






            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });









    }



    private void logOut() {
        LoginManager.getInstance().logOut();
        //changeD TO MATCH SHAREDPREF TO ENSURE THAT USER/FIRST/LAST ARE ALL CLEARED

        sharedPref.edit().remove("user").commit();
        sharedPref.edit().remove("firstName").commit();
        sharedPref.edit().remove("lastName").commit();
        sharedPref.edit().remove("inTransaction").commit();

        //takes us back to LoginActivity
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void addTransaction(Transaction tr) {transactionList.add(tr);}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("??");
        if (resultCode == Activity.RESULT_OK) {
            System.out.println("?");
            if (data.getParcelableExtra("newTransaction") != null) {
                System.out.println("!");

                Transaction tr = (Transaction) data.getParcelableExtra("newTransaction");
                System.out.println(tr);
                transactionList.add(tr);
                mAdapter.notifyDataSetChanged();
            }
        }
    }



}

