package com.example.sabrinapin.lendit;

/**
 * Created by sabrinapin on 4/19/18.
 */

public class User {

    private final String USER_FIRST;
    private final String USER_LAST;
    private final String USER_ID;

    public User(String firstName, String lastName, String userID){
        USER_FIRST = firstName;
        USER_LAST = lastName;
        USER_ID = userID;
    }

    public String getUSER_FIRST(){
        return USER_FIRST;
    }

    public String getUSER_LAST(){
        return USER_LAST;
    }

    public String getUSER_ID(){
        return USER_ID;
    }
}
