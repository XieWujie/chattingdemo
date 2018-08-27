package com.example.xiewujie.chattingdemo.service;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;
import com.avos.avoscloud.im.v2.AVIMMessage;
import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class MyConversationHandler extends AVIMConversationEventHandler {

    private static MyConversationHandler eventHandler;

    public static synchronized MyConversationHandler getInstance() {
        if (null == eventHandler) {
            eventHandler = new MyConversationHandler();
        }
        return eventHandler;
    }

    private MyConversationHandler() {
    }

    @Override
    public void onUnreadMessagesCountUpdated(AVIMClient client, AVIMConversation conversation) {
//        LCIMConversationItemCache.getInstance().insertConversation(conversation.getConversationId());
//        EventBus.getDefault().post(new LCIMOfflineMessageCountChangeEvent());
    }

    @Override
    public void onLastDeliveredAtUpdated(AVIMClient client, AVIMConversation conversation) {
//        LCIMConversationReadStatusEvent event = new LCIMConversationReadStatusEvent();
//        event.conversationId = conversation.getConversationId();
//        EventBus.getDefault().post(event);
    }

    @Override
    public void onLastReadAtUpdated(AVIMClient client, AVIMConversation conversation) {
//        LCIMConversationReadStatusEvent event = new LCIMConversationReadStatusEvent();
//        event.conversationId = conversation.getConversationId();
       // EventBus.getDefault().post(event);
    }

    @Override
    public void onMemberLeft(AVIMClient client, AVIMConversation conversation, List<String> members, String kickedBy) {
        // 因为不同用户需求不同，此处暂不做默认处理，如有需要，用户可以通过自定义 Handler 实现
    }

    @Override
    public void onMemberJoined(AVIMClient client, AVIMConversation conversation, List<String> members, String invitedBy) {
    }

    @Override
    public void onKicked(AVIMClient client, AVIMConversation conversation, String kickedBy) {
    }

    @Override
    public void onInvited(AVIMClient client, AVIMConversation conversation, String operator) {
    }

    @Override
    public void onMessageRecalled(AVIMClient client, AVIMConversation conversation, AVIMMessage message) {
//        EventBus.getDefault().post(new LCIMMessageUpdatedEvent(message));
    }

    @Override
    public void onMessageUpdated(AVIMClient client, AVIMConversation conversation, AVIMMessage message) {
//        EventBus.getDefault().post(new LCIMMessageUpdatedEvent(message));
    }


}
