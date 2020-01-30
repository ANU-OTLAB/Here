package com.otlab.here.option.model;

public class WaitingAcceptItem {
    private String friendName;
    private String time;
    private String validity;

    public WaitingAcceptItem(String friendName, String time, String validity){
        this.friendName = friendName;
        this.time = time;
        this.validity = validity;
    }
    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

}
