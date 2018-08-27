package com.example.xiewujie.chattingdemo.view.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.bumptech.glide.Glide;
import com.example.xiewujie.chattingdemo.R;

import java.io.File;

public class ChatImageHolder extends ChatItemHolder {

    protected ImageView imageView;
    private static final int MAX_DEFAULT_HEIGHT = 400;
    private static final int MAX_DEFAULT_WIDTH = 300;

    public ChatImageHolder(Context context, ViewGroup root, boolean isLeft) {
        super(context, root, isLeft);
    }

    @Override
    public void initView() {
        super.initView();
        conventLayout.addView(View.inflate(getContext(),R.layout.chat_image_message_layout, null));
        imageView = (ImageView) itemView.findViewById(R.id.chat_image_view);
        if (isLeft) {
           // imageView.setBackgroundResource(R.drawable.lcim_chat_item_left_bg);
        } else {
          //  imageView.setBackgroundResource(R.drawable.lcim_chat_item_right_bg);
        }

    }

    @Override
    public void bindData(Object o) {
        super.bindData(o);
        imageView.setImageResource(0);
        AVIMMessage message = (AVIMMessage) o;
        if (message instanceof AVIMImageMessage) {
            AVIMImageMessage imageMsg = (AVIMImageMessage) message;
            String localPath = imageMsg.getFileUrl();
            double actualHight = imageMsg.getHeight();
            double actualWidth = imageMsg.getWidth();

            double viewHeight = MAX_DEFAULT_HEIGHT;
            double viewWidth = MAX_DEFAULT_WIDTH;

            if (0 != actualHight && 0 != actualWidth) {
                // 要保证图片的长宽比不变
                double ratio = actualHight / actualWidth;
                if (ratio > viewHeight / viewWidth) {
                    viewHeight = (actualHight > viewHeight ? viewHeight : actualHight);
                    viewWidth = viewHeight / ratio;
                } else {
                    viewWidth = (actualWidth > viewWidth ? viewWidth : actualWidth);
                    viewHeight = viewWidth * ratio;
                }
            }

            imageView.getLayoutParams().height = (int) viewHeight;
            imageView.getLayoutParams().width = (int) viewWidth;

            if (!TextUtils.isEmpty(localPath)) {
               Glide.with(getContext().getApplicationContext()).load(localPath);
                     //   resize((int) viewWidth, (int) viewHeight).centerCrop().into(imageView);
            } else if (!TextUtils.isEmpty(imageMsg.getFileUrl())) {
               Glide.with(getContext().getApplicationContext()).load(imageMsg.getFileUrl());
                      //  resize((int) viewWidth, (int) viewHeight).centerCrop().into(imageView);
            } else {
                imageView.setImageResource(R.drawable.personal_default_background);
            }
        }
    }
}
