package com.otlab.here.option.model;

// FriendListItem을 이루는 정보

public class FriendItem {
    private String userId;
    private String userName;
    private String validity;

    public FriendItem(String userId, String userName, String validity){
        this.userId = userId;
        this.userName = userName;
        this.validity = validity;
    }
    public String getUserId() {
        return userId;
    }
    public String getUserName() {
        return userName;
    }
    public String getValidity() {return validity;}

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setValidity(String validity) {this.validity = validity;}
}
