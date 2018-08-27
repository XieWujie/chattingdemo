package com.example.xiewujie.chattingdemo.presenter;

import android.content.Context;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.xiewujie.chattingdemo.view.view.AddView;

import java.util.List;

public class AddPresenter extends BasePresenter<AddView> {

    public AddPresenter(Context mContext, AddView view) {
        super(mContext, view);
    }

   public void getValues(String key){
        AVQuery<AVObject> query = new AVQuery<>("_User");
        query.whereContains("username",key);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e==null) {
                    view.beforeGetValues();
                    for (AVObject object : list) {
                        view.getValues(object);
                    }
                    view.onFinish();
                }else {
                    view.onFail(e);
                }
            }
        });
    }
}
