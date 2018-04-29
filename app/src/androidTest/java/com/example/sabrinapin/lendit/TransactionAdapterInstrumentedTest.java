package com.example.sabrinapin.lendit;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
/**
 * Created by sabrinapin on 4/28/18.
 */

public class TransactionAdapterInstrumentedTest {

    private String[] mPeople;
    private String [] mDates;
    private String[] mObjects;
    private Bitmap[] mImages;
    private Context mContext;
    private TransactionAdapter mTransactionAdapter;

    public void makeTransactionAdapter() {
        mPeople = new String [2];
        mPeople [0] = "Gus";
        mPeople [1] = "Theo";

        mDates = new String [2];
        mDates [0] = "May";
        mDates [1] = "April";

        mObjects = new String [2];
        mObjects [0] = "book";
        mObjects [1] = "shirt";

        //making tester bitmap objects
        Bitmap b = Bitmap.createBitmap(12, 12, Bitmap.Config.ARGB_8888);
        Bitmap b2 = Bitmap.createBitmap(12, 12, Bitmap.Config.ARGB_8888);

        mImages = new Bitmap [2];
        mImages [0] = b;
        mImages [1] = b2;

        //CHECK OUT INSTRUMENTAL UNIT TEST FOR FAKE CONTEXT
        mContext = InstrumentationRegistry.getTargetContext();

        mTransactionAdapter = new TransactionAdapter(mContext, mPeople, mDates, mObjects, mImages);
    }


    //put in methods later

    //tests all methods associated with "people" String array
    @Test
    public void transactionAdapterPeople () throws Exception {
        //test methods
        //assertequals statement
    }

    //tests all methods associated with "date" String array
    @Test
    public void transactionAdapterDates () throws Exception {
        //test methods
        //assertequals statement
    }

    //tests all methods associated with "object" String array
    @Test
    public void transactionAdapterObjects () throws Exception {
        //test methods
        //assertequals statement
    }

    //tests all methods associated with "image" Bitmap array
    @Test
    public void transactionAdapterImages () throws Exception {
        //test methods
        //assertequals statement
    }
}
