package com.example.xiewujie.chattingdemo.model.user;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.example.xiewujie.chattingdemo.view.event.HeaderInitEvent;
import com.example.xiewujie.chattingdemo.view.event.OwnerDataInitEvent;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.Map;

public class OwnerManager {

    private Owner owner;
    private OwnerManager(){
        List<Owner> list = DataSupport.limit(1).find(Owner.class);
        if (list==null||list.size()<1){
            owner = new Owner();
        }else {
            owner = list.get(0);
        }

    }


    public static class UserManagerHelper{
        private final static OwnerManager USER_MANAGER = new OwnerManager();
        public static OwnerManager getInstance(){
            return USER_MANAGER;
        }
    }

    public Owner getOwner() {
        return owner;
    }

    public void initOwner(JSONObject jsonObject, Map<String,Object> userInfo, final String userId){
        try {
            if (jsonObject==null)return;
            final String nickname = jsonObject.getString("nickname");
            final String gender = jsonObject.getString("gender");
            final int age = 2018-Integer.valueOf(jsonObject.getString("year"));
            final String location = jsonObject.getString("province")+jsonObject.getString("city");
            final String avatarUrl = jsonObject.getString("figureurl");
            if (owner!=null){
                owner.setAvatarUrl(avatarUrl);
                EventBus.getDefault().post(new HeaderInitEvent(avatarUrl));
                owner.setGender(gender);
                owner.setName(nickname);
                owner.setAge(age);
                owner.setLocation(location);
                owner.setUserId(userId);
                owner.setLogIn(true);
                owner.setUserInfo(userInfo);
            }
            AVObject object = AVObject.createWithoutData("_User",userId);
            object.put("username",nickname);
            object.put("gender",gender);
            object.put("user_header_image",avatarUrl);
            object.put("userId",userId);
            object.put("age",age);
            object.put("location",location);
            owner.save();
            object.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e!=null){
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initOwner(final String userId){
        MyContactProvider.ContactProviderHelper.getInstance().getUserById(userId, new GetUserListener() {
            @Override
            public void done(ChatUser chatUser, Exception e) {
               owner.setLogIn(true);
               owner.setGender(chatUser.getGender());
               owner.setUserId(userId);
               owner.setAge(chatUser.getAge());
               owner.setName(chatUser.getName());
               owner.setLocation(chatUser.getLocation());
                String avatarUrl = chatUser.getAvatarUrl();
                EventBus.getDefault().post(new HeaderInitEvent(avatarUrl));
               owner.setAvatarUrl(avatarUrl);
               owner.setBackgroundUrl(chatUser.getBackgroudUrl());
            }
        });
    }

    public<T> void updateOwner(String[] key,T[] values){
        if (key==null||values==null||key.length!=values.length){
            return;
        }
        AVObject avObject = AVObject.createWithoutData("_User",owner.getUserId());
        for (int i = 0;i<key.length;i++){
            avObject.put(key[i],values[i]);
        }
        avObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e!=null){
                    e.printStackTrace();
                }else {
                    initOwner(owner.getUserId());
                }
            }
        });
    }
}

