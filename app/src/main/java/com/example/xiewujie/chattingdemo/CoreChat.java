package com.example.xiewujie.chattingdemo;


import android.content.Context;
import android.text.TextUtils;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUtils;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.example.xiewujie.chattingdemo.model.user.MyContactProvider;

public final class CoreChat {

  private static CoreChat coreChat;
  private MyContactProvider profileProvider;
  private String currentUserId;

  private CoreChat() {
  }

  public static synchronized CoreChat getInstance() {
    if (null == coreChat) {
      coreChat = new CoreChat();
    }
    return coreChat;
  }

  public void init(Context context, String appId, String appKey) {
    if (TextUtils.isEmpty(appId)) {
      throw new IllegalArgumentException("appId can not be empty!");
    }
    if (TextUtils.isEmpty(appKey)) {
      throw new IllegalArgumentException("appKey can not be empty!");
    }

    AVOSCloud.initialize(context.getApplicationContext(), appId, appKey);

  }

  public void setContactsProvider(MyContactProvider profileProvider) {
    this.profileProvider = profileProvider;
  }


  public MyContactProvider getProfileProvider() {
    return profileProvider;
  }



  public void open(final String userId, final AVIMClientCallback callback) {
    open(userId, null, callback);
  }


    public void open(final String userId, String tag, final AVIMClientCallback callback) {
        if (TextUtils.isEmpty(userId)) {
            throw new IllegalArgumentException("userId can not be empty!");
        }
        if (null == callback) {
            throw new IllegalArgumentException("callback can not be null!");
        }

        AVIMClientCallback openCallback = new AVIMClientCallback() {
            @Override
            public void done(final AVIMClient avimClient, AVIMException e) {
                if (null == e) {
                    MyContactProvider.ContactProviderHelper.getInstance().provideContacts(userId);
                    currentUserId = userId;
                    callback.internalDone(avimClient,e);
                } else {
                    callback.internalDone(avimClient, e);
                }
            }
        };

        if (AVUtils.isBlankContent(tag)) {
            AVIMClient.getInstance(userId).open(openCallback);
        } else {
            AVIMClient.getInstance(userId, tag).open(openCallback);
        }
    }


  public void close(){
//    AVIMClient.getInstance(currentUserId).close(new AVIMClientCallback() {
//      @Override
//      public void done(AVIMClient avimClient, AVIMException e) {
//        currentUserId = null;
//        if (null != callback) {
//          callback.internalDone(avimClient, e);
//        }
//      }
//    });
  }

  public String getCurrentUserId() {
    return currentUserId;
  }


  public AVIMClient getClient() {
    if (!TextUtils.isEmpty(currentUserId)) {
      return AVIMClient.getInstance(currentUserId);

    }
    return null;
  }
}
