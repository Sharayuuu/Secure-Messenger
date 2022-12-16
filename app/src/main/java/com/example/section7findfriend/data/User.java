package com.example.section7findfriend.data;

import android.net.Uri;

public class User {
    public String userId;
    public String fullName;
    public String email;
    public String age;

    public User(){

    }

    public User(String userId, String fullName, String email, String Age){
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.age = Age;

    }
    //-------------------------------------------------------------------------

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(String age) {
        this.age = age;
    }





    public String getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getAge() {
        return age;
    }
}


