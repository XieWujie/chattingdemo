package com.example.xiewujie.chattingdemo.view.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.bumptech.glide.Glide;
import com.example.xiewujie.chattingdemo.R;
import com.example.xiewujie.chattingdemo.model.user.ChatUser;
import com.example.xiewujie.chattingdemo.model.user.GetUserListener;
import com.example.xiewujie.chattingdemo.model.user.MyContactProvider;
import com.example.xiewujie.chattingdemo.view.activity.PersonalActivity;
import com.example.xiewujie.chattingdemo.view.event.ResendMessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

public class  ChatItemHolder extends RecyclerView.ViewHolder{

    protected boolean isLeft;
    protected AVIMMessage message;
    protected ImageView avatarView;
    protected TextView timeView;
    protected TextView nameView;
    protected LinearLayout conventLayout;
    protected FrameLayout statusLayout;
    protected ProgressBar progressBar;
    protected TextView statusView;
    protected ImageView errorView;
    private String conversationId;

    public ChatItemHolder(Context context, ViewGroup root, boolean isLeft) {
        super(LayoutInflater.from(context).inflate(isLeft ? R.layout.chat_item_left_layout : R.layout.lcim_chat_item_right_layout,root,false));
        this.isLeft = isLeft;
        initView();
    }

    protected Context getContext(){
        return itemView.getContext();
    }

    public void initView() {
        if (isLeft) {
            avatarView = (ImageView) itemView.findViewById(R.id.chat_left_iv_avatar);
            timeView = (TextView) itemView.findViewById(R.id.chat_left_tv_time);
            nameView = (TextView) itemView.findViewById(R.id.chat_left_tv_name);
            conventLayout = (LinearLayout) itemView.findViewById(R.id.chat_left_layout_content);
            statusLayout = (FrameLayout) itemView.findViewById(R.id.chat_left_layout_status);
            statusView = (TextView) itemView.findViewById(R.id.chat_left_tv_status);
            progressBar = (ProgressBar) itemView.findViewById(R.id.chat_left_progressbar);
            errorView = (ImageView) itemView.findViewById(R.id.chat_left_tv_error);
        } else {
            avatarView = (ImageView) itemView.findViewById(R.id.chat_right_iv_avatar);
            timeView = (TextView) itemView.findViewById(R.id.chat_right_tv_time);
            nameView = (TextView) itemView.findViewById(R.id.chat_right_tv_name);
            conventLayout = (LinearLayout) itemView.findViewById(R.id.chat_right_layout_content);
            statusLayout = (FrameLayout) itemView.findViewById(R.id.chat_right_layout_status);
            progressBar = (ProgressBar) itemView.findViewById(R.id.chat_right_progressbar);
            errorView = (ImageView) itemView.findViewById(R.id.chat_right_tv_error);
            statusView = (TextView) itemView.findViewById(R.id.chat_right_tv_status);
        }

        setAvatarClickEvent();
        setResendClickEvent();
        setUpdateMessageEvent();
    }


    public void bindData(Object o) {
        message = (AVIMMessage) o;
        conversationId = message.getFrom();
        timeView.setText(millisecsToDateString(message.getTimestamp()));
        nameView.setText("");
        MyContactProvider.ContactProviderHelper.getInstance().getUserById(message.getFrom(), new GetUserListener() {
            @Override
            public void done(ChatUser chatUser, Exception e) {
                Glide.with(getContext()).load(chatUser.getAvatarUrl()).error(R.drawable.lcim_default_avatar_icon).into(avatarView);
            }
        });
        switch (message.getMessageStatus()) {
            case AVIMMessageStatusFailed:
                statusLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                statusView.setVisibility(View.GONE);
                errorView.setVisibility(View.VISIBLE);
                break;
            case AVIMMessageStatusSent:
                statusLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                statusView.setVisibility(View.VISIBLE);
                statusView.setVisibility(View.GONE);
                errorView.setVisibility(View.GONE);
                break;
            case AVIMMessageStatusSending:
                statusLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                statusView.setVisibility(View.GONE);
                errorView.setVisibility(View.GONE);
                break;
            case AVIMMessageStatusNone:
            case AVIMMessageStatusReceipt:
                statusLayout.setVisibility(View.GONE);
                break;
        }
    }



    /**
     * 设置头像点击按钮的事件
     */
    private void setAvatarClickEvent() {
        avatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),PersonalActivity.class);
                intent.putExtra("uerId",conversationId);
                getContext().startActivity(intent);
            }
        });
    }

    /**
     * 设置发送失败的叹号按钮的事件
     */
    private void setResendClickEvent() {
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ResendMessageEvent(message));
            }
        });
    }

    private void setUpdateMessageEvent() {
        conventLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
               return false;
            }
        });
    }

    private static String millisecsToDateString(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        return format.format(new Date(timestamp));
    }
}
