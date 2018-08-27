package com.example.xiewujie.chattingdemo.model.user;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.xiewujie.chattingdemo.CoreChat;
import com.example.xiewujie.chattingdemo.view.event.AddContactEvent;
import com.tencent.qc.stat.common.User;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class MyContactProvider {

    private HashSet<ChatUser> allUsers;
    private HashSet<String> allIds;

    private MyContactProvider(){
        allUsers = new HashSet<>();
        allIds = new HashSet<>();
    }

   public static class ContactProviderHelper{
        public static final MyContactProvider PROVIDER = new MyContactProvider();
        public static MyContactProvider getInstance(){
            return PROVIDER;
        }
    }

    public void provideContacts(String id){
        List<ChatUser> chatUsers = DataSupport.findAll(ChatUser.class);
        if (chatUsers.size()>0){
            allUsers.addAll(chatUsers);
             Iterator<ChatUser> iterable = allUsers.iterator();
             if (iterable.hasNext()){
                 allIds.add(iterable.next().getUserId());
             }
            return;
        }
        EventBus.getDefault().post(new AddContactEvent());
        AVQuery avQuery = new AVQuery("Contacts");
        avQuery.whereEqualTo("userId",id);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e==null&&list!=null){
                 for (AVObject object:list){
                     ChatUser chatUser = new ChatUser();
                     String contactId = object.getString("contactId");
                     chatUser.setUserId(contactId);
                     chatUser.setRemarkName(object.getString("contactRemarkName"));
                     allUsers.add(chatUser);
                     allIds.add(contactId);
                 }
                 getContactsMessage(allIds);
                }
            }
        });
    }

    public void getContactsMessage(final HashSet<String> idList){
        AVQuery query = new AVQuery("_User");
        query.whereContainedIn("objectId",idList);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e==null&&list!=null){
                    for (int i = 0,size = list.size();i<size;i++){
                        AVObject object = list.get(i);
                        String id = object.getObjectId();
                        String userName = object.getString("username");
                        String headerUrl = object.getString("user_header_image");
                        for (ChatUser user:allUsers){
                            if (id.equals(user.getUserId())){
                                user.setName(userName);
                                user.setAvatarUrl(headerUrl);
                                user.save();
                                break;
                            }
                        }
                    }
                    EventBus.getDefault().post(new AddContactEvent());
                }
            }
        });
    }

    public void addContact(final String id,final String remarkName){
        String userId = CoreChat.getInstance().getCurrentUserId();
        final AVObject addObject = new AVObject("Contacts");
        addObject.put("userId",userId);
        addObject.put("contactId",id);
        addObject.put("contactRemarkName",remarkName);
       addObject.saveInBackground(new SaveCallback() {
           @Override
           public void done(AVException e) {
               if (e==null){
                   Log.d("contacts","保存成功");
                   allIds.add(id);
               }else {
                   Log.d("contacts","保存失败");
               }
           }
       });
        AVQuery avQuery = new AVQuery("_User");
        avQuery.whereEqualTo("userId",id);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (list!=null&&e==null&&list.size()>0){
                    AVObject object = list.get(0);
                    ChatUser chatUser = new ChatUser();
                    chatUser.setName(object.getString("username"));
                    chatUser.setRemarkName(remarkName);
                    String avatarUrl = object.getString("user_header_image");
                    chatUser.setAvatarUrl(avatarUrl);
                    chatUser.setUserId(id);
                    chatUser.setAge(object.getInt("age"));
                    chatUser.setBackgroudUrl(object.getString("background_image"));
                    chatUser.setLocation(object.getString("location"));
                    allUsers.add(chatUser);
                    chatUser.save();
                    EventBus.getDefault().post(new AddContactEvent());
                }
            }
        });
    }
    public HashSet<ChatUser> getAllUsers(){
        return allUsers;
    }

    public HashSet<String> getAllIds() {
        return allIds;
    }

    public void getUserById(final String id, final GetUserListener listener){
        if (id==null){
            listener.done(null,new Exception("找不到联系人"));
            return;
        }
        ChatUser user = null;
        for (ChatUser user1:allUsers){
            if (user1.getUserId().equals(id)){
                user = user1;
                break;
            }
        }
        if (user!=null){
           listener.done(user,null);
        }else {
            AVQuery avQuery = new AVQuery("_User");
            avQuery.whereEqualTo("userId",id);
            avQuery.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (list!=null&&e==null&&list.size()>0){
                        AVObject object = list.get(0);
                        ChatUser chatUser = new ChatUser();
                        chatUser.setName(object.getString("username"));
                        chatUser.setAvatarUrl(object.getString("user_header_image"));
                        chatUser.setUserId(id);
                        chatUser.setAge(object.getInt("age"));
                        chatUser.setBackgroudUrl(object.getString("background_image"));
                        chatUser.setLocation(object.getString("location"));
                        listener.done(chatUser,null);
                    }else {
                        listener.done(null,e);
                    }
                }
            });
        }
    }

}