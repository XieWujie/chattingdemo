package com.example.xiewujie.chattingdemo.view.fragment;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMMessageOption;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.example.xiewujie.chattingdemo.R;
import com.example.xiewujie.chattingdemo.service.MyMessageHandler;
import com.example.xiewujie.chattingdemo.service.MyService;
import com.example.xiewujie.chattingdemo.view.adapter.ChatRcAdapter;
import com.example.xiewujie.chattingdemo.view.customerview.InputButtonView;
import com.example.xiewujie.chattingdemo.view.event.BarTextEvent;
import com.example.xiewujie.chattingdemo.view.event.PictureEvent;
import com.example.xiewujie.chattingdemo.view.event.ResendMessageEvent;
import com.example.xiewujie.chattingdemo.view.event.TypeMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;

public class ChatFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    protected AVIMConversation imConversation;
    protected ChatRcAdapter itemAdapter;
    protected RecyclerView recyclerView;
    protected LinearLayoutManager layoutManager;
    protected SwipeRefreshLayout refreshLayout;
    protected InputButtonView inputBottomBar;
    private static final int OPEN_ALBUM = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragmetn_layout, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_chat_rc_view);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_chat_fresh_view);
        refreshLayout.setEnabled(false);
        inputBottomBar = (InputButtonView) view.findViewById(R.id.fragment_chat_inputbottombar);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        itemAdapter = getAdapter();
        itemAdapter.resetRecycledViewPoolSize(recyclerView);
        recyclerView.setAdapter(itemAdapter);
        EventBus.getDefault().register(this);
        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class,new MyMessageHandler());
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AVIMMessage message = itemAdapter.getFirstMessage();
                if (null == message) {
                    refreshLayout.setRefreshing(false);
                } else {
                    imConversation.queryMessages(message.getMessageId(), message.getTimestamp(), 20, new AVIMMessagesQueryCallback() {
                        @Override
                        public void done(List<AVIMMessage> list, AVIMException e) {
                            refreshLayout.setRefreshing(false);
                            if (e==null) {
                                if (null != list && list.size() > 0) {
                                    itemAdapter.addMessageList(list);
                                    itemAdapter.setDeliveredAndReadMark(imConversation.getLastDeliveredAt(),
                                            imConversation.getLastReadAt());
                                    itemAdapter.notifyDataSetChanged();
                                    layoutManager.scrollToPositionWithOffset(list.size() - 1, 0);
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    protected ChatRcAdapter getAdapter() {
        return new ChatRcAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != imConversation) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        AVIMMessageManager.unregisterMessageHandler(AVIMTypedMessage.class,new MyMessageHandler());
    }

    public void setConversation(final AVIMConversation conversation) {
        imConversation = conversation;
        refreshLayout.setEnabled(true);
        inputBottomBar.setTag(imConversation.getConversationId());
        fetchMessages();
        cn.leancloud.chatkit.utils.LCIMNotificationUtils.addTag(conversation.getConversationId());
        if (!conversation.isTransient()) {
            if (conversation.getMembers().size() == 0) {
                conversation.fetchInfoInBackground(new AVIMConversationCallback() {
                    @Override
                    public void done(AVIMException e) {
                        if (null != e) {

                        }
                        itemAdapter.showUserName(conversation.getMembers().size() > 2);
                    }
                });
            } else {
                itemAdapter.showUserName(conversation.getMembers().size() > 2);
            }
        } else {
            itemAdapter.showUserName(true);
        }
    }


    private void fetchMessages() {
        imConversation.queryMessages(new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> messageList, AVIMException e) {
                if (e == null) {
                    itemAdapter.setMessageList(messageList);
                    recyclerView.setAdapter(itemAdapter);
                    itemAdapter.setDeliveredAndReadMark(imConversation.getLastDeliveredAt(),
                            imConversation.getLastReadAt());
                    itemAdapter.notifyDataSetChanged();
                    scrollToBottom();
                    clearUnread();
                }
            }
        });
    }
    private void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(itemAdapter.getItemCount() - 1, 0);
    }

    private void clearUnread() {
        if (imConversation.getUnreadMessagesCount() > 0) {
            imConversation.read();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BarTextEvent event){
        sendText(event.getContent());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TypeMessageEvent event){

        itemAdapter.addMessage(event.getMessage());
        itemAdapter.notifyDataSetChanged();
        scrollToBottom();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PictureEvent event){
        dispatchPictureIntent();
    }

    public void onEvent(ResendMessageEvent event){
        AVIMMessage message = event.getMessage();
        if (null != imConversation && null != event &&
                null !=message && imConversation.getConversationId().equals(message.getConversationId())) {
            if (AVIMMessage.AVIMMessageStatus.AVIMMessageStatusFailed == message.getMessageStatus()
                    && imConversation.getConversationId().equals(message.getConversationId())) {
                sendMessage(message, false);
            }
        }
    }

    private void sendText(String content){
        AVIMTextMessage textMessage = new AVIMTextMessage();
        textMessage.setText(content);
        sendMessage(textMessage,true);
    }

    public void sendMessage(AVIMMessage message, boolean addToList) {
        if (addToList) {
            itemAdapter.addMessage(message);
        }
        itemAdapter.notifyDataSetChanged();
        scrollToBottom();
        AVIMMessageOption option = new AVIMMessageOption();
        option.setReceipt(true);
        imConversation.sendMessage(message, option, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                itemAdapter.notifyDataSetChanged();
                if (null != e) {
                  e.printStackTrace();
                }
            }
        });
    }

    public void sendImageMessage(Uri uri) {
        {
            String realPath = MyService.getRealPathFromURI(getContext(),uri);
            try {
                AVIMImageMessage imageMessage = new AVIMImageMessage(realPath);
                sendMessage(imageMessage,true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void dispatchPictureIntent() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, null);
        photoPickerIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(photoPickerIntent, OPEN_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case OPEN_ALBUM:
                  sendImageMessage(data.getData());
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
