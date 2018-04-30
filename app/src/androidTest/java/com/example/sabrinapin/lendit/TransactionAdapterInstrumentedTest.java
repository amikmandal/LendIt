package com.example.sabrinapin.lendit;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;
/**
 * Created by sabrinapin on 4/28/18.
 */

public class TransactionAdapterInstrumentedTest {

    List<TransFirInfo> list;
    private Context mContext;
    private TransactionAdapter mTransactionAdapter;

    public void makeTransactionAdapter() {
        TransFirInfo a = new TransFirInfo();
        a.setowner("Joe");

        TransFirInfo b = new TransFirInfo();
        b.setowner("Amik");


        mContext = InstrumentationRegistry.getTargetContext();

       // mTransactionAdapter = new TransactionAdapter(mContext, list);
    }


    //test getLength method
}
