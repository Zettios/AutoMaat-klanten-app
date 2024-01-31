package com.example.auto_maatklantenapp.helper_classes;

public enum NavSelection {
    CAR_LIST(1),
    CAR_RESERVATION(2),
    ACCIDENT_REPORT(3),
    SUPPORT(4);

    private final int numVal;

    NavSelection(int numVal){
        this.numVal = numVal;
    }

    public int getNumVal(){
        return numVal;
    }
}