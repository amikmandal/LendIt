package com.example.sabrinapin.lendit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import java.util.List;

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

    private boolean rebuild;

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
        rebuild = getIntent().getBooleanExtra("rebuild", false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Intent intent = getIntent();
        USER_FIRST_NAME = sharedPref.getString("firstName", "dumb");
        USER_LAST_NAME = sharedPref.getString("lastName", "dumber");
        userID = sharedPref.getString("user", "dumbest");

        FDB = FirebaseDatabase.getInstance();

        myRef = mFirebaseDatabase.getReference().child("usernames").child(userID);



//        List<EventObject> eObjs = sql.getAllEvents();
//        for(EventObject eobj : eObjs)  {
//            System.out.println("*****");
//            System.out.println("*****");
//            System.out.println("*****");
//            System.out.println("*****");
//            System.out.println(eobj.toString());
//            System.out.println("*****");
//            System.out.println("*****");
//            System.out.println("*****");
//            System.out.println("*****");
//        }
//
////        sql.deleteDB();
//
//        List<EventObject> eObjs2 = sql.getAllEvents();
//        for(EventObject eobj : eObjs2)  {
//            System.out.println("*****");
//            System.out.println("*****");
//            System.out.println("*****");
//            System.out.println("*****");
//            System.out.println(eobj.toString());
//            System.out.println("*****");
//            System.out.println("*****");
//            System.out.println("*****");
//            System.out.println("*****");
//        }



//        logout = findViewById(R.id.button2);
//        //setup using preference manager
//        logout.setText("Logout Here "+USER_FIRST_NAME+" "+USER_LAST_NAME+" "+userID);
//
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                logOut();
//            }
//        });

        //Toast.makeText(this, mUser.getUSER_FIRST(), Toast.LENGTH_SHORT).show();

        if(sharedPref.contains("inTransaction")){
            intent = new Intent(this, NewTransaction.class);
            this.startActivity(intent);
            Toast.makeText(this, "inTransaction is in sharedPref: FIX THIS", Toast.LENGTH_LONG).show();
            finish();
        }

        if(!sharedPref.contains("inTransaction")){
            Toast.makeText(this, "inTransaction is NOT in sharedPref", Toast.LENGTH_LONG).show();
        }



//        mPeople = new String[transactionList.size()];
//        mObjects = new String[transactionList.size()];
//        mDates = new String[transactionList.size()];
//        mUris = new Uri[transactionList.size()];




        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ArrayList<TransFirInfo> trialList = new ArrayList<TransFirInfo>();
        TransFirInfo first = new TransFirInfo();
        first.setowner("werido");
        first.setdate("3/3/3");
        first.setborrower("people");
        first.setitem("shit");
        first.setImageUrl("https://firebasestorage.googleapis.com/v0/b/lendit-af1e0.appspot.com/o/JPEG_20180429_074637_1948933449012095423.jpg?alt=media&token=720d7752-e0f3-4c5e-84b1-7e18354580cb");
        trialList.add(first);
        mAdapter = new TransactionAdapter(this, trialList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                        SQLiteJDBC db = new SQLiteJDBC(getApplicationContext());
                        int length = (int) dataSnapshot.getChildrenCount();

                        ArrayList<TransFirInfo> myArr = new ArrayList<TransFirInfo>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            TransFirInfo user = snapshot.getValue(TransFirInfo.class);
                            if(rebuild)  {
                                EventObject e = new EventObject(user.getborrower() + " returns " + user.getitem() + " to " + user.getowner(), user.getdate());
                                db.addEvent(e);
                            }
                            myArr.add(0, user);
                            Log.d("borrower", user.getitem());


                        }
                        db.closeDB();
                        rebuild = false;
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


//    private void showData(DataSnapshot dataSnapshot) {
//        for (DataSnapshot ds: dataSnapshot.getChildren()){
//            Log.d("firebase", ds.child(userID).toString()  + ds.child(userID));
//    UsernameInformation uInfo = ds.child(userID).getValue(UsernameInformation.class);
//
////    uInfo.setName(ds.child(userID).getValue(UsernameInformation.class).getName());
////    uInfo.setLenditUsername(ds.child(userID).getValue(UsernameInformation.class).getLenditUsername());
//    ArrayList <String> thisList = new ArrayList ();
//    thisList.add(uInfo.getLenditUsername());
//    thisList.add(uInfo.getName());
//    mTextMessage.setText(thisList.toString());


//        }

//    }

    private void logOut() {
        LoginManager.getInstance().logOut();
        //changeD TO MATCH SHAREDPREF TO ENSURE THAT USER/FIRST/LAST ARE ALL CLEARED

        sharedPref.edit().remove("user").commit();
        sharedPref.edit().remove("firstName").commit();
        sharedPref.edit().remove("lastName").commit();
        sharedPref.edit().remove("inTransaction").commit();

        SQLiteJDBC sql = new SQLiteJDBC(getApplicationContext());

        System.out.println("*****");
        System.out.println("*****");
        System.out.println("*****");
        System.out.println("*****");
        System.out.println("*****");
        System.out.println("*****");
        System.out.println("*****");
        System.out.println("*****");
        System.out.println(sql.getAllEvents().size());
        System.out.println("*****");
        System.out.println("*****");
        System.out.println("*****");
        System.out.println("*****");
        System.out.println("*****");
        System.out.println("*****");
        System.out.println("*****");
        System.out.println("*****");

        sql.onUpgrade(sql.getReadableDatabase(), 1, 2);


//        getApplicationContext().deleteDatabase(SQLiteJDBC.getTableName());

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


//    public void initList(ArrayList<TransFirInfo> transactionList)  {
//
//
//        mPeople = new String[transactionList.length];
//        mObjects = new String[transactionList.length];
//        mDates = new String[transactionList.length];
//        mUris = new Uri[transactionList.length];
//
//        if(transactionList.length > 0) {
//            System.out.println("Did we make it");
//            for (int i = 0; i < transactionList.length; i++) {
//                mPeople[i] = transactionList[i].getowner();
//                mObjects[i] = transactionList[i].getitem();
//                mDates[i] = transactionList[i].getdate();
//                mUris[i] = Uri.parse(transactionList[i].getImageUrl());
//
//            }
//        }
//
//        mAdapter.notifyDataSetChanged();
//
//    }

//    void getDateFireBase(){
//
//        DBR = FDB.getReference("https://lendit-af1e0.firebaseio.com/").child("users");
//
//        DBR.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//                TransFirInfo myObject = dataSnapshot.getValue(TransFirInfo.class);
//                myArr.add(myObject);
//                mRecyclerView.setAdapter(new TransactionAdapter(getApplicationContext(), myArr));
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }

    //Create Menu
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


}

