package com.example.sabrinapin.lendit;

/**
 * Created by sramprasad on 4/26/2018.
 */

public class UsernameInformation
{
    private String name;
    private String lenditUsername;

    //used in UsernameActivity to store current user's unique username and first and last name (stored as "name")
    public UsernameInformation(){


    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getlenditUsername() {
        return lenditUsername;
    }

    public void setlenditUsername(String lenditUsername) {
        this.lenditUsername = lenditUsername;
    }


}
