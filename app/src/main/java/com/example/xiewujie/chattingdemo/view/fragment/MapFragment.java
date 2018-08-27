package com.example.xiewujie.chattingdemo.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMMessageOption;
import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.example.xiewujie.chattingdemo.CoreChat;
import com.example.xiewujie.chattingdemo.R;
import com.example.xiewujie.chattingdemo.model.convert.MapChat;
import com.example.xiewujie.chattingdemo.model.user.ChatUser;
import com.example.xiewujie.chattingdemo.model.user.GetUserListener;
import com.example.xiewujie.chattingdemo.model.user.MyContactProvider;
import com.example.xiewujie.chattingdemo.service.MyMessageHandler;
import com.example.xiewujie.chattingdemo.view.event.InitPositionEvent;
import com.example.xiewujie.chattingdemo.view.event.MapTextEvent;
import com.example.xiewujie.chattingdemo.view.event.TypeMessageEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class MapFragment extends Fragment implements LocationSource,AMapLocationListener,View.OnClickListener,AMap.OnMarkerClickListener{


    private AMap aMap;
    private MapView mapView;
    private com.amap.api.maps.LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private boolean isFirst = true;
    private Button start;
    private  AVIMMessageOption option;
    private HashMap<String,Marker> allMarker;
    private boolean isClick = false;
    private AVIMClient client;
    private List<String> contactsIds = new ArrayList<>();
    private AVIMConversation conversation;
    private  AVIMLocationMessage locationMessage;
    private HashMap<String,MapChat> allMap = new HashMap<>();
    private AVIMTextMessage textMessage;
    private String ownerId;
    private MapChat ownerChat;
    private int m = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.map_fragment_layout,container,false);
        mapView = (MapView) view.findViewById(R.id.map);
        start = (Button)view.findViewById(R.id.start);
        allMarker = new HashMap<>();
        start.setOnClickListener(this);
        mapView.onCreate(savedInstanceState);
        initMap();
        return view;
    }

    private void initMap(){
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        setUpMap();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start:
                isClick = true;
                init();
                break;
        }
    }

    /**
     * 设置地图属性
     */
    private void setUpMap(){
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);// 跟随模式
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null){
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0){
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                if (isFirst){
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 18));//定位成功移到当前定位点
                    isFirst = false;
                }
                double longitude = aMapLocation.getLongitude();
                double latitude = aMapLocation.getLatitude();
                if (isClick){ sharePosition(latitude,longitude);
                  if (ownerChat==null){
                      ownerChat = new MapChat(latitude,longitude,ownerId,"我","hi");
                  }
                    updateMarker(latitude,longitude,R.drawable.map_location_owner,"我"+m,ownerChat.getContent(),false);
                }
            }else{
                Log.i("123",aMapLocation.getErrorCode()+"错误码"+aMapLocation.getErrorInfo()+"错误信息");
            }
        }
    }

    //激活定位
    @Override
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null){
            mlocationClient = new AMapLocationClient(getContext());
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mlocationClient.setLocationOption(mLocationOption);// 设置为高精度定位模式
            mLocationOption.setInterval(5000);
            mlocationClient.setLocationListener(this);// 设置定位监听
            mlocationClient.startLocation();
            aMap.setOnMarkerClickListener(this);
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }


    /**
     * 方法必须重写
     */


    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEvent(InitPositionEvent event){
        show("位置"+event.getUserId());
        initPosition(event.getUserId(),event.getLatitude(),event.getLongitude());
    }

    private void initPosition(final String userId, final double lat, final double lot){
        if (allMap.containsKey(userId)){
            MapChat mapChat = allMap.get(userId);
            updateMarker(lat,lot,R.drawable.map_location_other,mapChat.getName(),mapChat.getContent(),false);
        }else {
            MyContactProvider.ContactProviderHelper.getInstance().getUserById(userId, new GetUserListener() {
                @Override
                public void done(ChatUser chatUser, Exception e) {
                    MapChat mapChat = new MapChat();
                    if (chatUser.getRemarkName()!=null&&chatUser.getRemarkName().length()>0){
                        mapChat.setName(chatUser.getRemarkName());
                    }else {
                        mapChat.setName(chatUser.getName());
                    }
                    mapChat.setLatitude(lat);
                    mapChat.setLongitude(lot);
                    mapChat.setUserId(userId);
                    mapChat.setContent("hi");
                    allMap.put(userId,mapChat);
                    updateMarker(lat,lot,R.drawable.map_location_other,mapChat.getName(),mapChat.getContent(),false);
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEvent(MapTextEvent event){
       sendText(event.getContent());
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEvent(TypeMessageEvent event){
        AVIMTypedMessage message = event.getMessage();
        if (message.getMessageType()== AVIMReservedMessageType.TextMessageType.getType()){
            initText((AVIMTextMessage)message);
        }
    }

    private void initText(AVIMTextMessage message){
        String userId = message.getFrom();
        show(message.getText());
        if (allMap.containsKey(userId)){
            MapChat mapChat = allMap.get(userId);
            updateMarker(mapChat.getLatitude(),mapChat.getLongitude(),R.drawable.map_location_other,mapChat.getName(),message.getText(),true);
        }
    }

    private void show(String content){
        Snackbar.make(start,content,Snackbar.LENGTH_LONG).show();
    }

    private void sendText(final String content){
        textMessage = new AVIMTextMessage();
        textMessage.setText(content);
        if (conversation!=null){
            conversation.sendMessage(textMessage,option, new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    if (e!=null){
                        e.printStackTrace();
                    }else {
                        updateMarker(content);
                    }
                }
            });
        }else {
            init();
        }
    }

    private void updateMarker(String content){
        if (ownerChat==null){
            init();
            return;
        }

        updateMarker(ownerChat.getLatitude(),ownerChat.getLongitude(),R.drawable.map_location_owner,"我",content,true);
        ownerChat.setContent(content);
    }

    public void updateMarker(double latitude, double longitude,int id,String title,String content,boolean showInfo){

        MarkerOptions markOptions = new MarkerOptions().
                position(new LatLng(latitude,longitude))
                .icon(BitmapDescriptorFactory.fromResource(id))
                .title(title)
                .snippet(content);
        if (allMarker.containsKey(title)){
            Marker marker = allMarker.get(title);
            marker.remove();
            marker = (Marker)aMap.addMarker(markOptions);
            allMarker.put(title,marker);
            if (showInfo) {
                marker.showInfoWindow();
            }
        }else {
            Marker marker = (Marker)aMap.addMarker(markOptions);
            allMarker.put(title,marker);
            if (showInfo) {
                marker.showInfoWindow();
            }
        }
    }


    private void sharePosition( double latitude,double longitude){
        if (conversation!=null&&locationMessage!=null){
            locationMessage = new AVIMLocationMessage();
            locationMessage.setLocation(new AVGeoPoint(latitude,longitude));
            conversation.sendMessage(locationMessage,option, new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    if (e!=null) {
                        Snackbar.make(start, "位置分享失败", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }else {
            init();
        }
    }

    private void init(){
        client = CoreChat.getInstance().getClient();
        locationMessage = new AVIMLocationMessage();
        textMessage = new AVIMTextMessage();
        option = new AVIMMessageOption();
        option.setReceipt(true);
        HashSet<String> hashSet = MyContactProvider.ContactProviderHelper.getInstance().getAllIds();
        contactsIds.addAll(hashSet);
        client.createConversation(contactsIds, "map chat", null, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation avimConversation, AVIMException e) {
                conversation = avimConversation;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        AVIMMessageManager.unregisterMessageHandler(AVIMTypedMessage.class,new MyMessageHandler());
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class,new MyMessageHandler());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }
}