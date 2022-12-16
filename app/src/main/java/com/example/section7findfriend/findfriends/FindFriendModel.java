package com.example.section7findfriend.findfriends;

public class FindFriendModel {
    private String userName;
    private String userId;
    private boolean requestSent;

    public FindFriendModel(String userName, String userId, boolean requestSent) {
        this.userName = userName;

        this.userId = userId;
        this.requestSent = requestSent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }





    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isRequestSent() {
        return requestSent;
    }

    public void setRequestSent(boolean requestSent) {
        this.requestSent = requestSent;
    }
}
