package com.example.sabrinapin.lendit;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.AbstractList;
import java.util.List;

import static org.junit.Assert.*;
/**
 * Created by sabrinapin on 4/28/18.
 */

public class TransactionAdapterInstrumentedTest {


    private Context mContext;
    List<TransFirInfo> list;
    private int listLength;
    private TransactionAdapter mTransactionAdapter;

    public void makeTransactionAdapter() {
        TransFirInfo a = new TransFirInfo();
        a.setowner("joe");

        TransFirInfo b = new TransFirInfo();
        b.setowner("amik");

       // list = new List<TransFirInfo>() ;

      //  list.set(0,a);
        //list.set(1,b);

       // listLength = 2;

        //CHECK OUT INSTRUMENTAL UNIT TEST FOR FAKE CONTEXT
        mContext = InstrumentationRegistry.getTargetContext();

        //mTransactionAdapter = new TransactionAdapter(mContext, list);
    }

    //tests getLength method
    @Test
    public void transactionAdapterLength () throws Exception {
        //test methods
        makeTransactionAdapter();
        //assertEquals statement
      //  assertEquals(list.size(), mTransactionAdapter.getItemCount());
    }


}
