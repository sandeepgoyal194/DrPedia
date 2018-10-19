package com.softmine.drpedia.home.model;

import java.io.Serializable;
import java.util.ArrayList;

public class UserInterestTypes implements Serializable {

    public ArrayList<Integer> getSubtype_id() {
        return subtype_id;
    }

    public void setSubtype_id(ArrayList<Integer> subtype_id) {
        this.subtype_id = subtype_id;
    }

    ArrayList<Integer> subtype_id;

}
