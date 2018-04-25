package com.example.sabrinapin.lendit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private static final int GET_TRANSACTION = 0;
    private ArrayList<Transaction> transactionList;
    private TextView mTextMessage;
    private static final String TAG = "MainActivity";
    private TransactionAdapter mAdapter;
    String[] mPeople;
    String[] mObjects;
    String[] mDates;
    Bitmap[] mImages;


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
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        transactionList = new ArrayList<Transaction>();

        mPeople = new String[transactionList.size()];
        mObjects = new String[transactionList.size()];
        mDates = new String[transactionList.size()];
        mImages = new Bitmap[transactionList.size()];

        RecyclerView rv = findViewById(R.id.recyclerView);

        mAdapter = new TransactionAdapter(this, mPeople, mObjects, mDates, mImages);
        rv.setAdapter(mAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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
                initList();
                mAdapter.notifyDataSetChanged();
            }
        }
    }


    public void initList()  {


        mPeople = new String[transactionList.size()];
        mObjects = new String[transactionList.size()];
        mDates = new String[transactionList.size()];
        mImages = new Bitmap[transactionList.size()];

        if(transactionList.size() > 0) {
            System.out.println("Did we make it");
            for (int i = 0; i < transactionList.size(); i++) {
                mPeople[i] = transactionList.get(i).getOwner();
                mObjects[i] = transactionList.get(i).getItem();
                mDates[i] = transactionList.get(i).getDate();
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(transactionList.get(i).getImage());
                mImages[i] = BitmapFactory.decodeStream(arrayInputStream);
            }
        }

    }




}
