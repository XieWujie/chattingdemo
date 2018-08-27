package com.example.xiewujie.chattingdemo.view.event;

public class HeaderInitEvent {

    String avatarUrl;

    public HeaderInitEvent(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
