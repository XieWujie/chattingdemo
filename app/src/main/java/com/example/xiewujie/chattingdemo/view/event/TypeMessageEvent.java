package com.example.xiewujie.chattingdemo.view.event;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

public class TypeMessageEvent {

    private AVIMConversation conversation;
    private AVIMTypedMessage message;

    public AVIMConversation getConversation() {
        return conversation;
    }

    public void setConversation(AVIMConversation conversation) {
        this.conversation = conversation;
    }

    public AVIMTypedMessage getMessage() {
        return message;
    }

    public void setMessage(AVIMTypedMessage message) {
        this.message = message;
    }

    public TypeMessageEvent(AVIMConversation conversation, AVIMTypedMessage message) {

        this.conversation = conversation;
        this.message = message;
    }
}
