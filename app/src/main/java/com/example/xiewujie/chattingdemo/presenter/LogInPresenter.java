package com.example.xiewujie.chattingdemo.presenter;

import android.app.Activity;
import android.content.Context;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.sns.SNS;
import com.avos.sns.SNSBase;
import com.avos.sns.SNSCallback;
import com.avos.sns.SNSException;
import com.avos.sns.SNSType;
import com.example.xiewujie.chattingdemo.CoreChat;
import com.example.xiewujie.chattingdemo.model.user.Owner;
import com.example.xiewujie.chattingdemo.model.user.OwnerManager;
import com.example.xiewujie.chattingdemo.view.view.LogInView;

import org.litepal.crud.DataSupport;

import java.util.Map;

public class LogInPresenter extends BasePresenter<LogInView> {

    public LogInPresenter(Context context,LogInView logInView) {
        super(context,logInView);
    }

    public void logIn(final String userName, String password){
        AVUser.logInInBackground(userName, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    final AVIMClient client = AVIMClient.getInstance(avUser);
                    CoreChat.getInstance().open(client.getClientId(), new AVIMClientCallback() {
                        @Override
                        public void done(AVIMClient avimClient, AVIMException e) {
                            if (e == null) {
                                Owner owner = OwnerManager.UserManagerHelper.getInstance().getOwner();
                                if (owner.getName()==null){
                                    OwnerManager.UserManagerHelper.getInstance().initOwner(avimClient.getClientId());
                                }else {
                                    if (!owner.getName().equals(userName)){
                                        DataSupport.deleteAll(Owner.class);
                                        OwnerManager.UserManagerHelper.getInstance().initOwner(avimClient.getClientId());
                                    }
                                }
                                view.onFinish();
                            } else {
                                view.onFail(e);
                            }
                        }
                    });
                }else {
                    view.onFail(e);
                }
            }
        });
    }
    public void qqLogin(final Activity activity){
        try {
            SNS.setupPlatform(mContext,SNSType.AVOSCloudSNSQQ, "1107783980","hUr0zNOkrQTwzSQ8","https://leancloud.cn");
            SNS.loginWithCallback(activity, SNSType.AVOSCloudSNSQQ, new SNSCallback() {
                @Override
                public void done(final SNSBase base, SNSException e) {
                    if (e == null) {
                        if (base.authorizedData()!=null){
                            final Map<String,Object> userInfo = base.userInfo();
                         SNS.loginWithAuthData(userInfo, new LogInCallback<AVUser>() {
                            @Override
                            public void done(final AVUser user, AVException e) {
                                if (e == null) {
                                    AVIMClient client = AVIMClient.getInstance(user);
                                    String userId = client.getClientId();
                                    final String objectId = user.getObjectId();

                                    CoreChat.getInstance().open(userId, new AVIMClientCallback() {
                                        @Override
                                        public void done(AVIMClient avimClient, AVIMException e) {
                                            if (e == null) {
                                                view.onFinish();
                                                OwnerManager.UserManagerHelper.getInstance().initOwner(base.authorizedData(),userInfo,objectId);
                                            } else {
                                                view.onFail(e);
                                            }
                                        }
                                    });
                                } else {
                                    e.printStackTrace();
                                    view.onFail(e);
                                }
                            }
                        });

                        }
                    }
                }
            });
        }catch (AVException e){
            view.onFail(e);
        }
    }

    public void logInWithAuthorData(Map<String,Object> userInfo){
        SNS.loginWithAuthData(userInfo, new LogInCallback<AVUser>() {
            @Override
            public void done(final AVUser user, AVException e) {
                if (e == null) {
                    AVIMClient client = AVIMClient.getInstance(user);
                    final String userId = client.getClientId();
                    CoreChat.getInstance().open(userId, new AVIMClientCallback() {
                        @Override
                        public void done(AVIMClient avimClient, AVIMException e) {
                            if (e == null) {
                                view.onFinish();
                                OwnerManager.UserManagerHelper.getInstance().initOwner(userId);
                            } else {
                                view.onFail(e);
                            }
                        }
                    });
                } else {
                    e.printStackTrace();
                    view.onFail(e);
                }
            }
        });
    }
}

