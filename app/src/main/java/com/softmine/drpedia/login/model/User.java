package com.softmine.drpedia.login.model;

import java.io.Serializable;

public class User implements Serializable {


    String userid;
    String name;
    String emailid;
    String photourl;
    String gender;
    String dob;
    String authToken;
    String mobile_no;

    public User() {
    }

    public User(User user) {
        userid = user.getUserid();
        name = user.getName();
        photourl = user.getPhotoUrl();
        gender = user.getGender();
        mobile_no = user.getMobile_no();
        emailid = user.getEmailid();
        dob = user.getDob();
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }



    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPhotoUrl() {
        return photourl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photourl = photoUrl;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
