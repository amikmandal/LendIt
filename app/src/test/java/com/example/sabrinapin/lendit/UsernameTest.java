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

    public void makeUsername(){
       name = "Katie";
        lenditUsername = "katiekat";
        mUsername = new UsernameInformation();
        mUsername.setname(name);
        mUsername.setlenditUsername(lenditUsername);

    }

    //all "set" methods are already used in makeUsername method

    @Test
    public void userName() throws Exception {
        makeUsername();
        assertEquals(name, mUsername.getname());
    }

    @Test
    public void userLenditName() throws Exception {
        makeUsername();
        assertEquals(lenditUsername, mUsername.getlenditUsername());
    }


}
