package com.otlab.here.setting.model;

public class SettingItem {

    private String settingName;
    private String distance;
    private String destination;
    private String validity;
    public SettingItem(String settingName, String distance, String destination, String validity) {
        this.settingName = settingName;
        this.distance = distance;
        this.destination = destination;
        this.validity = validity;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) { this.settingName = settingName; }

    public String getDistance() { return distance; }

    public void setDistance(String distance) { this.distance = distance; }

    public String getDestination() { return destination; }

    public void setDestination(String destination) { this.destination = destination; }

    public String getValidity() { return validity; }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public enum ServiceType {
        CREATE, UPDATE, DELETE
    }
    public enum DestinationType{
        PERSON, PLACE, UNKNOWN
    }
}
