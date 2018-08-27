package com.example.xiewujie.chattingdemo.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;


import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.example.xiewujie.chattingdemo.CoreChat;
import com.example.xiewujie.chattingdemo.R;
import com.example.xiewujie.chattingdemo.constant.Conversation;
import com.example.xiewujie.chattingdemo.model.user.ChatUser;
import com.example.xiewujie.chattingdemo.model.user.GetUserListener;
import com.example.xiewujie.chattingdemo.model.user.MyContactProvider;
import com.example.xiewujie.chattingdemo.view.fragment.ChatFragment;

import java.util.Arrays;

public class ChatActivity extends BaseActivity {

    private ChatFragment chatFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatFragment = (ChatFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_chat);
        initView();
        initConversation();
    }

    private void initView(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.chat_tool_bar);
        setSupportActionBar(toolbar);
    }

    private void initConversation(){
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey(Conversation.WHAT_CHAT_ID)){
            createConversation(bundle.getString(Conversation.WHAT_CHAT_ID));
        }
    }

    private void createConversation(String contactId){
        MyContactProvider.ContactProviderHelper.getInstance().getUserById(contactId, new GetUserListener() {
            @Override
            public void done(final ChatUser chatUser, Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setTitle(chatUser.getName());
                    }
                });
            }
        });
        CoreChat.getInstance().getClient().createConversation(Arrays.asList(contactId), "", null, false, true, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation avimConversation, AVIMException e) {
                if (e==null){
                    setConversation(avimConversation);
                }
            }
        });
    }

    private void setConversation(AVIMConversation conversation){
        chatFragment.setConversation(conversation);
    }
}
