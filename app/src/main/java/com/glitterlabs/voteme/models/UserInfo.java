package com.glitterlabs.voteme.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Home on 11/4/2016.
 */

public class UserInfo {

    public String image;
    public String firstName;
    public String lastName;
    public String number;
    public String vote;
    public String feedback;
    public HashMap<String,Boolean> hashMap=new HashMap<>();
    public UserInfo(){

    }



    public UserInfo(String image, String firstName, String lastName, String userId,String number,String vote,String feedback) {
        this.image = image;
        this.firstName = firstName;
        this.lastName = lastName;
        this.number=number;
        this.vote=vote;
        this.feedback=feedback;


    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
