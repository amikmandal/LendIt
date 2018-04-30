package com.example.sabrinapin.lendit;

/**
 * Created by sabrinapin on 4/28/18.
 */

import org.junit.Test;
import static org.junit.Assert.*;

public class UsernameTest {

    private String name;
    private String lenditUsername;
    private UsernameInformation mUsername;

    //creates new UsernameInformation object using "set" methods
    public void makeUsername(){
        name = "Katie";
        lenditUsername = "katiekat";
        mUsername = new UsernameInformation();
        mUsername.setname(name);
        mUsername.setlenditUsername(lenditUsername);

    }

    //all "set" methods are already used in makeUsername method

    //checks to see if setname and getname work correctly
    @Test
    public void userName() throws Exception {
        makeUsername();
        assertEquals(name, mUsername.getname());
    }

    //checks to see if setlenditUsername and getlenditUsername work correctly
    @Test
    public void userLenditName() throws Exception {
        makeUsername();
        assertEquals(lenditUsername, mUsername.getlenditUsername());
    }


}
