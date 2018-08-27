package com.example.xiewujie.chattingdemo.service;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import com.example.xiewujie.chattingdemo.CoreChat;
import com.example.xiewujie.chattingdemo.view.event.InitPositionEvent;
import com.example.xiewujie.chattingdemo.view.event.TypeMessageEvent;

import org.greenrobot.eventbus.EventBus;

public class MyMessageHandler extends AVIMTypedMessageHandler<AVIMTypedMessage>{

    @Override
    public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {

        if (message==null||conversation.getConversationId()==null){
            return;
        }
        if (CoreChat.getInstance().getCurrentUserId()==null){
            client.close(null);
        }else {
            if (!CoreChat.getInstance().getCurrentUserId().equals(client.getClientId())){
                client.close(null);
            }
            if (!CoreChat.getInstance().getCurrentUserId().equals(message.getFrom())){
                TypeMessageEvent messageEvent = new TypeMessageEvent(conversation,message);
                EventBus.getDefault().post(messageEvent);
                if (message.getMessageType() == AVIMReservedMessageType.LocationMessageType.getType()){
                    AVIMLocationMessage locationMessage = (AVIMLocationMessage)message;
                   postLocationMessage(locationMessage);
                }
            }
        }
    }

    private void postLocationMessage(AVIMLocationMessage message){
        String userId = message.getFrom();
        AVGeoPoint point = message.getLocation();
        EventBus.getDefault().post(new InitPositionEvent(point.getLatitude(),point.getLongitude(),userId));
    }
}
