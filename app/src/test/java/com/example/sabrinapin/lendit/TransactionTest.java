package com.example.sabrinapin.lendit;

/**
 * Created by sabrinapin on 4/28/18.
 */

import org.junit.Test;
import static org.junit.Assert.*;

public class TransactionTest {

    private String mOwner, mItem, mDate;
    private byte[] mImage;
    private Transaction mTransaction;

    //creates a new Transaction using parameters
    public void makeTransaction(){
        mOwner = "George";
        mItem = "book";
        mDate = "March";

        //hexString taken from stackoverflow
        mImage = hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d");

        mTransaction = new Transaction(mOwner, mItem, mDate, mImage);
    }


    //make tests for getOwner, getItem, getDate and getImage

    //checks if setOwner and getOwner works
    @Test
    public void transactionOwner() throws Exception {
        makeTransaction();
        assertEquals(mOwner, mTransaction.getOwner());
    }

    //tests getItem - sees if it correctly stores and retrieves item
    @Test
    public void transactionItem() throws Exception {
        makeTransaction();
        assertEquals(mItem, mTransaction.getItem());
    }

    //tests if actually stores/gets date
    @Test
    public void transactionDate() throws Exception {
        makeTransaction();
        assertEquals(mOwner, mTransaction.getOwner());
    }

    //tests if it correctly stores/gets image
    @Test
    public void transactionImage() throws Exception {
        makeTransaction();
        assertEquals(mImage, mTransaction.getImage());
    }

    //tests toString method
    @Test
    public void testString(){
        makeTransaction();
        String toS = mOwner + " " + mItem + " " + mDate + " " + mImage;
        assertEquals(toS, mTransaction.toString());
    }

    //ASK NIALL ABOUT PARCELS
    //nevermind - we don't use parcels!

    //method taken from Dave L in "Convert a string represenation of a hex dump to a byte array using Java?"
    //this was in a stackoverflow post
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
