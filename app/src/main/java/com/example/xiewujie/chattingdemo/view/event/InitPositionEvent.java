package com.example.xiewujie.chattingdemo.view.event;

public class InitPositionEvent {

    private double longitude;
    private double latitude;
    private String userId;

    public InitPositionEvent(double longitude, double latitude, String userId) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.userId = userId;
    }

    public InitPositionEvent() {
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
