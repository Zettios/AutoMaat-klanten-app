package com.example.auto_maatklantenapp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(RobolectricTestRunner.class)
public class LoginActivityTest {
    LoginActivity loginActivity;

    @Before
    public void initMocks() {
        loginActivity = new LoginActivity();
    }

    @Test
    public void validateLoginDataTest() {
        assertTrue(loginActivity.validateLoginData("username", "password"));
    }

    @Test
    public void validateLoginDataInvalidTest() {
        assertFalse(loginActivity.validateLoginData("", ""));
    }
}
