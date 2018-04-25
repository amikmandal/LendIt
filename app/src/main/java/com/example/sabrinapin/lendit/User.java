package com.example.sabrinapin.lendit;

/**
 * Created by sabrinapin on 4/19/18.
 */

public class User {

    private  String USER_FIRST;
    private String USER_LAST;
    private String USER_ID;
    private String USERNAME;

    public User(String firstName, String lastName, String userID){
        USER_FIRST = firstName;
        USER_LAST = lastName;
        USER_ID = userID;
    }

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
