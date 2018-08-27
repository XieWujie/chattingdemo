package com.example.xiewujie.chattingdemo.presenter;


import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataStreamCallback;
import com.bumptech.glide.Glide;
import com.example.xiewujie.chattingdemo.R;
import com.example.xiewujie.chattingdemo.model.convert.Contact;
import com.example.xiewujie.chattingdemo.model.user.ChatUser;
import com.example.xiewujie.chattingdemo.model.user.GetUserListener;
import com.example.xiewujie.chattingdemo.model.user.MyContactProvider;
import com.example.xiewujie.chattingdemo.view.activity.ContactVerifyActivity;
import com.example.xiewujie.chattingdemo.view.view.PersonalView;

import java.io.InputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalPresenter extends BasePresenter<PersonalView> {

    public PersonalPresenter(Context mContext, PersonalView view) {
        super(mContext, view);
    }

    public void getData(String userId){
        MyContactProvider.ContactProviderHelper.getInstance().getUserById(userId, new GetUserListener() {
            @Override
            public void done(ChatUser chatUser, Exception e) {
                if (e==null){
                    view.getData(chatUser);
                }
            }
        });
    }
}
