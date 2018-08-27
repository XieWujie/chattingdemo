package com.example.xiewujie.chattingdemo.view.event;

import com.avos.avoscloud.im.v2.AVIMMessage;

public class ResendMessageEvent {
    private AVIMMessage message;

    public ResendMessageEvent(AVIMMessage message) {
        this.message = message;
    }

    public AVIMMessage getMessage() {
        return message;
    }

    public void setMessage(AVIMMessage message) {
        this.message = message;
    }
}
