package com.example.sabrinapin.lendit;

/**
 * Created by sabrinapin on 4/19/18.
 */

public class User {

    private  String USER_FIRST;
    private String USER_LAST;
    private String USER_ID;
    private String USERNAME;

    //Creates a user object to store first name, last name and unique Facebook ID
    //First used in LoginActivity
    //Later called in UsernameActivity to retrieve first and last name, along with ID
    public User(String firstName, String lastName, String userID){
        USER_FIRST = firstName;
        USER_LAST = lastName;
        USER_ID = userID;
    }

    //used for instances that setting is necessary
    public User(){

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

    public void setUSER_FIRST(String first){
        USER_FIRST = first;
    }

    public void setUSER_ID (String id){
        USER_ID = id;
    }

    public void setUSER_LAST(String last){
        USER_LAST = last;
    }

    public void setUSERNAME(String username){ USERNAME = username;}
}
