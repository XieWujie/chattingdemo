package com.example.xiewujie.chattingdemo.model.convert;

public class MapChat {

    private double latitude;
    private double longitude;
    private String userId;
    private String name;
    private String content;


    public MapChat() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MapChat(double latitude, double longitude, String userId, String name, String content) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
        this.name = name;
        this.content = content;
    }
}
