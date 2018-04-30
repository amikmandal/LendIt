package com.example.sabrinapin.lendit;

/**
 * Created by sabrinapin on 4/28/18.
 */

import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    private User mUser;
    private String USER_FIRST;
    private String USER_LAST;
    private String USER_ID;

    //creates new user for testing
    public void makeUser(){
        USER_FIRST = "John";
        USER_LAST = "Cena";
        USER_ID = "19";

        mUser = new User(USER_FIRST, USER_LAST, USER_ID);
    }

    //checks to see if getUSER_FIRST and setUSER_FIRST work
    @Test
    public void userFirst() throws Exception {
        makeUser();
        assertEquals(USER_FIRST, mUser.getUSER_FIRST());
        String newFirst = "Jed";
        mUser.setUSER_FIRST(newFirst);
        assertEquals(newFirst,mUser.getUSER_FIRST());

    }

    //checks to see if getUSER_LAST and setUSER_LAST work
    @Test
    public void userLast() throws Exception {
        makeUser();
        assertEquals(USER_LAST, mUser.getUSER_LAST());
        String newLast = "Bush";
        mUser.setUSER_LAST(newLast);
        assertEquals(newLast, mUser.getUSER_LAST());

    }

    //checks to see if getUSER_ID and setUSER_ID work
    @Test
    public void userId() throws Exception {
        makeUser();
        assertEquals(USER_ID, mUser.getUSER_ID());
        String newId = "1738";
        mUser.setUSER_ID(newId);
        assertEquals(newId, mUser.getUSER_ID());
    }

}
