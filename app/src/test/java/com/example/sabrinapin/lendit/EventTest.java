package com.example.sabrinapin.lendit;

/**
 * Created by sabrinapin on 4/29/18.
 */

import org.junit.Test;
import static org.junit.Assert.*;

public class EventTest {
    private int id;
    private String title;
    private String date;
    private EventObject mEvent;
    private EventObject mIdEvent;
    private EventObject mBasicEvent;

    //creates an EventObject object that does not have ID input
    public void makeEvent() {
        title = "title";
        date = "april";
        mEvent = new EventObject(title, date);
    }

    //creates EventObject object that does have ID input
    public void makeIdEvent() {
        title = "title";
        date = "april";
        id = 21;
        mIdEvent = new EventObject(id, title, date);
    }

    //creates EventObject with no inputs
    public void makeBasicEvent(){
        mBasicEvent = new EventObject();
    }

    //tests all methods regarding IDs, including getID and setID for ALL EventObjects
    @Test
    public void eventID() throws Exception {
        int iDTest = 2;

       //tests for makeEvent
        makeEvent();
        mEvent.setId(iDTest);
        assertEquals(iDTest, mEvent.getId());

        //tests for makeIdEvent
        makeIdEvent();
        assertEquals(id, mIdEvent.getId());
        mIdEvent.setId(iDTest);
        assertEquals(iDTest, mIdEvent.getId());

        //tests for makeBasicEvent
        makeBasicEvent();
        mBasicEvent.setId(iDTest);
        assertEquals(iDTest, mBasicEvent.getId());
    }

    //tests all methods regarding dates, including getDate and setDate for ALL EventObjects
    @Test
    public void eventDate() throws Exception {
        String testDate = "may";

        //tests for makeEvent
        makeEvent();
        assertEquals(date, mEvent.getDate());
        mEvent.setDate(testDate);
        assertEquals(testDate, mEvent.getDate());

        //tests for makeIdEvent
        makeIdEvent();
        assertEquals(date, mIdEvent.getDate());
        mIdEvent.setDate(testDate);
        assertEquals(testDate, mIdEvent.getDate());

        //tests for makeBasicEvent
        makeBasicEvent();
        mBasicEvent.setDate(testDate);
        assertEquals(testDate, mBasicEvent.getDate());
    }

    //tests all methods regarding titles, including getTitle and setTitle for ALL EventObjects
    @Test
    public void eventTitle() throws Exception {
        String testTitle = "heyy";

        //tests for makeEvent
        makeEvent();
        assertEquals(title, mEvent.getTitle());
        mEvent.setTitle(testTitle);
        assertEquals(testTitle, mEvent.getTitle());

        //tests for makeIdEvent
        makeIdEvent();
        assertEquals(title, mIdEvent.getTitle());
        mIdEvent.setTitle(testTitle);
        assertEquals(testTitle, mIdEvent.getTitle());

        //tests for makeBasicEvent
        makeBasicEvent();
        mBasicEvent.setTitle(testTitle);
        assertEquals(testTitle, mBasicEvent.getTitle());

    }
}
