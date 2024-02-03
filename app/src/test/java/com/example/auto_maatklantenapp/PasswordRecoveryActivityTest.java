package com.example.auto_maatklantenapp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class PasswordRecoveryActivityTest {
    PasswordRecoveryActivity passwordRecoveryActivity;

    @Before
    public void initMocks() {
        passwordRecoveryActivity = new PasswordRecoveryActivity();
    }

    @Test
    public void isValidEmailAddressTest() {
        assertTrue(passwordRecoveryActivity.isValidEmailAddress("email@goodle.com"));
    }

    @Test
    public void isValidEmailAddressInvalidTest() {
        assertFalse(passwordRecoveryActivity.isValidEmailAddress(""));
        assertFalse(passwordRecoveryActivity.isValidEmailAddress("sdsad"));
    }
}
