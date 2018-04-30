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
import android.view.Menu;
import android.view.MenuInflater;
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
    private Button logout;
    private static final String TAG = "MainActivity";
    private TransactionAdapter mAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private DatabaseReference mtransRef;
    private String userID;
    private String mUsername;
    private ArrayList <String> gotUsername = new ArrayList<String>();


    private DatabaseReference DBR;
    private FirebaseDatabase FDB;

    //sets up bottom bar so users can navigate between activities
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_new_transaction:
                    Intent thisIntent = new Intent(MainActivity.this, NewTransaction.class);
                    startActivityForResult(thisIntent, GET_TRANSACTION);
                    return true;
                case R.id.navigation_notifications:
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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://lendit-af1e0.firebaseio.com/");

        //initializes sharedPref - this shared preference carries same information across all activities
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Intent intent = getIntent();

        //retireves user's information from shared preferences - "dumb", "dumber", "dumbest" are all dummy strings
        USER_FIRST_NAME = sharedPref.getString("firstName", "dumb");
        USER_LAST_NAME = sharedPref.getString("lastName", "dumber");
        userID = sharedPref.getString("user", "dumbest");

        //accesses Firebase
        FDB = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("usernames").child(userID);

        //if user exits app while in a transaction, when reopened the app will still be in NewTransaction
        if(sharedPref.contains("inTransaction")){
            intent = new Intent(this, NewTransaction.class);
            this.startActivity(intent);
            finish();
        }

        //If a user exits the app while in Calendar, the app will return to that activity
        if(sharedPref.contains("inCalendar")){
            intent = new Intent(this, CaldroidSampleActivity.class);
            this.startActivity(intent);
            finish();
        }

        //sets up bottom navigation bar
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Creates an ArrayList of TransFirInfo objects to be later used in creating a new Transaction Adapter
        ArrayList<TransFirInfo> trialList = new ArrayList<TransFirInfo>();
        TransFirInfo first = new TransFirInfo();
        first.setowner("werido");
        first.setdate("3/3/3");
        first.setborrower("people");
        first.setitem("item");
        first.setImageUrl("https://firebasestorage.googleapis.com/v0/b/lendit-af1e0.appspot.com/o/JPEG_20180429_074637_1948933449012095423.jpg?alt=media&token=720d7752-e0f3-4c5e-84b1-7e18354580cb");
        trialList.add(first);

        //Creates new TransactionAdapter
        mAdapter = new TransactionAdapter(this, trialList);

        //Makes list of transactions scrollable
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //this is called once wwith the initial value and again when this is updated
                Log.d("firebase", dataSnapshot.toString());

                UsernameInformation mInfo = dataSnapshot.getValue(UsernameInformation.class);
                Log.d("minfo", mInfo.getname());

                Log.d("username", mInfo.getlenditUsername());
                String myUsername = mInfo.getlenditUsername();
                //Log.d("instance", mUsername);


                //Places the username in Shared preferences, so that we can access it through the other activity
                mtransRef = mFirebaseDatabase.getReference().child("users").child(myUsername);

                //Catches an exception if the user doesn't exist in the database: leave the array as an empty one if the data does not exist


                mtransRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


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


    //Allows user to log out of Facebook
    private void logOut() {
        LoginManager.getInstance().logOut();

        //clears all information from sharedPreferences
        sharedPref.edit().remove("user").commit();
        sharedPref.edit().remove("firstName").commit();
        sharedPref.edit().remove("lastName").commit();
        sharedPref.edit().remove("inTransaction").commit();
        sharedPref.edit().remove("inCalendar").commit();
        sharedPref.edit().remove("inUserNameActivity").commit();

        //takes user back to LoginActivity
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    //Creates Menu of options
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    //Selecting Menu
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.LogOut:
                logOut();
                return true;
            case R.id.Refresh:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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

