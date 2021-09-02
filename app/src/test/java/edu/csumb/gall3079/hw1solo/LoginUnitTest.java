package edu.csumb.gall3079.hw1solo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class LoginUnitTest {

    private static final String FAKE_STRING = "Login was successful";

    @Test
    public void wrongUsername() {
        List<User> u =  new ArrayList<>();
        User uu = new User(1, "Leanne Graham", "Bret", "Sincere@april.biz");
        u.add(uu);
        assertEquals(false, LoginActivity.usernameCheck(u, "carollll", "", 0));    }

    @Test
    public void wrongPassword() {
        assertEquals(false, LoginActivity.passwordCheck("capitalswe101", "anything"));
    }

    @Test
    public void rightUsername() {
        List<User> u =  new ArrayList<>();
        User uu = new User(1, "Leanne Graham", "Bret", "Sincere@april.biz");
        u.add(uu);
        assertEquals(true, LoginActivity.usernameCheck(u, "Bret", "", 0));
    }

    @Test
    public void rightPassword() {
        assertEquals(true, LoginActivity.passwordCheck("capitalswe101", "capitalswe101"));
    }


}