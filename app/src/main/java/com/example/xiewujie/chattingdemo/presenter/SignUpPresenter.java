package com.example.xiewujie.chattingdemo.presenter;

import android.content.Context;
import android.graphics.Region;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.sns.SNSQQ;
import com.avos.sns.SNSType;
import com.example.xiewujie.chattingdemo.CoreChat;
import com.example.xiewujie.chattingdemo.model.user.ChatUser;
import com.example.xiewujie.chattingdemo.view.view.RegisteredView;

public class SignUpPresenter extends BasePresenter<RegisteredView> {


    public SignUpPresenter(Context mContext, RegisteredView view) {
        super(mContext, view);
    }

    public void signUp(final String userName, final String passWord, String email){
        AVUser user = new AVUser();
        user.setUsername(userName);
        user.setPassword(passWord);
        user.setEmail(email);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e==null){
                    AVUser.logInInBackground(userName, passWord, new LogInCallback<AVUser>() {
                        @Override
                        public void done(AVUser avUser, AVException e) {
                            if (e==null) {
                                String useId = AVIMClient.getInstance(avUser).getClientId();
                                final String objectId = avUser.getObjectId();
                                AVObject object = AVObject.createWithoutData("_User", objectId);
                                object.put("userId",useId);
                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e!=null){
                                            view.onFail(e);
                                            return;
                                        }
                                    }
                                });
                                CoreChat.getInstance().open(useId, new AVIMClientCallback() {
                                    @Override
                                    public void done(AVIMClient avimClient, AVIMException e) {
                                        if (e != null) {
                                            Toast.makeText(mContext, "自动登陆失败", Toast.LENGTH_LONG).show();
                                        } else {
                                            view.onFinish();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }else {
                    view.onFail(e);
                }
            }
        });
    }
}
