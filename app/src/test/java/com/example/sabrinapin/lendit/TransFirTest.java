package com.example.sabrinapin.lendit;

/**
 * Created by sabrinapin on 4/28/18.
 */


import org.junit.Test;
import static org.junit.Assert.*;

public class TransFirTest {
    private String borrower;
    private String date;
    private String item;
    private String owner;
    private TransFirInfo mTransFir;

    //creates new TransFirInfo object using the "set" methods
    public void makeTransFir() {
        borrower = "Steve";
        date = "May";
        item = "cookies";
        owner = "Andrew";

        mTransFir = new TransFirInfo();

        mTransFir.setborrower(borrower);
        mTransFir.setdate(date);
        mTransFir.setitem(item);
        mTransFir.setowner(owner);
    }

    //all of these tests automatically test the "set" methods, as they are used in makeTransFir

    //tests getborrower for TransFirInfo objects
    @Test
    public void borrowerName() throws Exception{
        makeTransFir();
        assertEquals(borrower, mTransFir.getborrower());
    }

    //tests getdate for TransFirInfo objects
    @Test
    public void date() throws Exception{
        makeTransFir();
        assertEquals(date, mTransFir.getdate());
    }

    //tests getitem for TransFirInfo objects
    @Test
    public void item() throws Exception{
        makeTransFir();
        assertEquals(item, mTransFir.getitem());
    }

    //tests getowner for TransFirInfo objects
    @Test
    public void ownerName() throws Exception{
        makeTransFir();
        assertEquals(owner, mTransFir.getowner());
    }

    //tests toString method
    @Test
    public void testString() throws Exception{
        makeTransFir();
        String toS = borrower + " " + date + " " + item + " " +  owner;
        assertEquals(toS, mTransFir.toString());
    }

}