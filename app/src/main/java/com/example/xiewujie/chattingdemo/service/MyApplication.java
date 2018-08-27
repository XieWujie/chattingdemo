package com.example.xiewujie.chattingdemo.service;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMMessageManagerHelper;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.example.xiewujie.chattingdemo.CoreChat;
import com.example.xiewujie.chattingdemo.view.activity.MainActivity;

import org.litepal.LitePalApplication;

public class MyApplication extends Application {

    private Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePalApplication.initialize(context);
        CoreChat.getInstance().init(context,"Ktk2x0my8HgogksCxiULOAHq-gzGzoHsz","B584aQBHAAGA8NlVv8NIl7Nz");
        AVOSCloud.setDebugLogEnabled(true);
        CrashCatchHandler.getInstance().init(context);
        PushService.setDefaultPushCallback(this, MainActivity.class);
        PushService.setAutoWakeUp(true);
        PushService.setDefaultChannelId(this, "default");
    }
}
