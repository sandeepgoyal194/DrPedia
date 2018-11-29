package com.softmine.drpedia.home.model;

import java.util.ArrayList;

public class UserUpdateInterestType {

    ArrayList<Integer> newIntrest;

    public ArrayList<Integer> getNewIntrest() {
        return newIntrest;
    }

    public void setNewIntrest(ArrayList<Integer> newIntrest) {
        this.newIntrest = newIntrest;
    }

    public ArrayList<Integer> getRemoveIntrest() {
        return removeIntrest;
    }

    public void setRemoveIntrest(ArrayList<Integer> removeIntrest) {
        this.removeIntrest = removeIntrest;
    }

    ArrayList<Integer> removeIntrest;

}
