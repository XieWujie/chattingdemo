package com.example.xiewujie.chattingdemo.view.event;

import com.example.xiewujie.chattingdemo.model.user.ChatUser;

public class GetUserEvent {
    private ChatUser user;

    public GetUserEvent(ChatUser user) {
        this.user = user;
    }

    public ChatUser getUser() {
        return user;
    }

    public void setUser(ChatUser user) {
        this.user = user;
    }
}
