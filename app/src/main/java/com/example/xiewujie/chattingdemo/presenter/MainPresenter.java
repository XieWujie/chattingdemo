package com.example.xiewujie.chattingdemo.presenter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.example.xiewujie.chattingdemo.CoreChat;
import com.example.xiewujie.chattingdemo.R;
import com.example.xiewujie.chattingdemo.model.user.Owner;
import com.example.xiewujie.chattingdemo.view.view.MainView;

import org.litepal.crud.DataSupport;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainPresenter extends BasePresenter<MainView> {

    public MainPresenter(Context mContext, MainView view) {
        super(mContext, view);
    }
}
