package com.example.xiewujie.chattingdemo.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.example.xiewujie.chattingdemo.CoreChat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatRcAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int ITEM_LEFT = 100;
    private final int ITEM_LEFT_TEXT = 101;
    private final int ITEM_LEFT_IMAGE = 102;
    private final int ITEM_LEFT_AUDIO = 103;
    private final int ITEM_LEFT_LOCATION = 104;
    private final int ITEM_RIGHT = 200;
    private final int ITEM_RIGHT_TEXT = 201;
    private final int ITEM_RIGHT_IMAGE = 202;
    private final int ITEM_UNKNOWN = 300;
    private final static long TIME_INTERVAL = 1000 * 60 * 3;
    private boolean isShowUserName = true;
    protected List<AVIMMessage> messageList = new ArrayList<AVIMMessage>();
    private long lastDeliveredAt = 0;
    private long lastReadAt = 0;
    public ChatRcAdapter() {
        super();
    }

    public void setMessageList(List<AVIMMessage> messages) {
        messageList.clear();
        if (null != messages) {
            messageList.addAll(messages);
        }
    }

    public void addMessageList(List<AVIMMessage> messages) {
        messageList.addAll(0, messages);
    }

    public void addMessage(AVIMMessage message) {
        messageList.addAll(Arrays.asList(message));
    }

    public void updateMessage(AVIMMessage message) {
        for (int i = 0; i < messageList.size(); i++) {
            if (messageList.get(i).getMessageId().equals(message.getMessageId())) {
                messageList.remove(i);
                messageList.add(i, message);
                notifyItemChanged(i);
                return;
            }
        }
    }

    public AVIMMessage getFirstMessage() {
        if (null != messageList && messageList.size() > 0) {
            return messageList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_LEFT:
            case ITEM_LEFT_TEXT:
                return new ChatTextHolder(parent.getContext(), parent, true);
            case ITEM_RIGHT:
            case ITEM_RIGHT_TEXT:
                return new ChatTextHolder(parent.getContext(), parent, false);
            case ITEM_LEFT_IMAGE:
                return new ChatImageHolder(parent.getContext(), parent, true);
            case ITEM_RIGHT_IMAGE:
                return new ChatImageHolder(parent.getContext(), parent, false);
            default:
                return new ChatTextHolder(parent.getContext(), parent, true);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ChatItemHolder) holder).bindData(messageList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        AVIMMessage message = messageList.get(position);
        if (null != message && message instanceof AVIMTypedMessage) {
            AVIMTypedMessage typedMessage = (AVIMTypedMessage) message;
            boolean isMe = fromMe(typedMessage);
            if (typedMessage.getMessageType() == AVIMReservedMessageType.TextMessageType.getType()) {
                return isMe ? ITEM_RIGHT_TEXT : ITEM_LEFT_TEXT;
            } else if (typedMessage.getMessageType() == AVIMReservedMessageType.ImageMessageType.getType()) {
                return isMe ? ITEM_RIGHT_IMAGE : ITEM_LEFT_IMAGE;
            } else {
                return isMe ? ITEM_RIGHT : ITEM_LEFT;
            }
        }
        return ITEM_UNKNOWN;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    private boolean shouldShowTime(int position) {
        if (position == 0) {
            return true;
        }
        long lastTime = messageList.get(position - 1).getTimestamp();
        long curTime = messageList.get(position).getTimestamp();
        return curTime - lastTime > TIME_INTERVAL;
    }

    private boolean shouldShowDelivered(int position) {
        if (null != messageList && messageList.size() > 0) {
            int size = messageList.size();
            if (position < size) {
                long curTime = messageList.get(position).getTimestamp();
                if (curTime < lastDeliveredAt) {
                    return position == size - 1 || lastDeliveredAt < messageList.get(position + 1).getTimestamp();
                }
            }
        }
        return false;
    }
    private boolean shouldShowRead(int position) {
        if (null != messageList && messageList.size() > 0) {
            int size = messageList.size();
            if (position < size) {
                long curTime = messageList.get(position).getTimestamp();
                if (curTime < lastReadAt) {
                    return position == size - 1 || lastReadAt < messageList.get(position + 1).getTimestamp();
                }
            }
        }
        return false;
    }

    public void showUserName(boolean isShow) {
        isShowUserName = isShow;
    }

    public void setDeliveredAndReadMark(long deliveredAt, long readAt) {
        lastDeliveredAt = deliveredAt;
        lastReadAt = readAt;
    }

    public void resetRecycledViewPoolSize(RecyclerView recyclerView) {
        recyclerView.getRecycledViewPool().setMaxRecycledViews(ITEM_LEFT_TEXT, 25);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(ITEM_LEFT_IMAGE, 10);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(ITEM_LEFT_AUDIO, 15);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(ITEM_LEFT_LOCATION, 10);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(ITEM_RIGHT_TEXT, 25);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(ITEM_RIGHT_IMAGE, 10);
    }

    protected boolean fromMe(AVIMTypedMessage msg) {
        String selfId = CoreChat.getInstance().getCurrentUserId();
        return msg.getFrom() != null && msg.getFrom().equals(selfId);
    }
}
