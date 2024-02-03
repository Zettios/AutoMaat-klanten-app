package com.example.auto_maatklantenapp;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import android.widget.Spinner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class AccidentRapportFragmentTest {
    @InjectMocks
    private AccidentRapportFragment accidentRapportFragment;


    // Doesn't work
    @Before
    public void initMocks() {
        accidentRapportFragment = AccidentRapportFragment.newInstance();
    }

    @Test
    public void validateDataTest() {
        when(accidentRapportFragment.rental.getSelectedItemId()).thenReturn(null);
        when(accidentRapportFragment.odoMeter.getText().toString().trim().matches("")).thenReturn(true);
        accidentRapportFragment.encodedImage = "";

        assertFalse(accidentRapportFragment.validateData());
    }
}
