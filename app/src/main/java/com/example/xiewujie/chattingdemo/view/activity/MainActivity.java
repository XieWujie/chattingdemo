package com.example.xiewujie.chattingdemo.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.xiewujie.chattingdemo.R;
import com.example.xiewujie.chattingdemo.presenter.MainPresenter;
import com.example.xiewujie.chattingdemo.service.ActivityManager;
import com.example.xiewujie.chattingdemo.view.adapter.FragmentAdapter;
import com.example.xiewujie.chattingdemo.view.event.HeaderInitEvent;
import com.example.xiewujie.chattingdemo.view.fragment.ContactFragment;
import com.example.xiewujie.chattingdemo.view.fragment.MapFragment;
import com.example.xiewujie.chattingdemo.view.fragment.MessageFragment;
import com.example.xiewujie.chattingdemo.view.view.MainView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends BaseActivity implements MainView,ViewPager.OnPageChangeListener{

    private ViewPager viewPager;
    private TextView contactText;
    private TextView messageText;
    private MainPresenter presenter;
    private FragmentPagerAdapter adapter;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private CircleImageView avatarView;
    private TextView labelText;
    private TextView mapText;
    private LinearLayout bottomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter(this,this);
        initView();
        requestPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void initView(){
        viewPager = $(R.id.main_view_pager);
        contactText = $(R.id.contact_text);
        messageText= $(R.id.message_text);
        drawerLayout = $(R.id.drawer_layout);
        toolbar = $(R.id.main_tool_bar);
        avatarView = $(R.id.main_avatar_view);
        labelText = $(R.id.main_label);
        mapText = $(R.id.map_text);
        bottomLayout = $(R.id.main_bottom_layout);
        initViewPager();
        contactText.setOnClickListener(this);
        messageText.setOnClickListener(this);
        avatarView.setOnClickListener(this);
        mapText.setOnClickListener(this);
        Glide.with(this)
                .load(R.drawable.header_default_icon)
                .into(avatarView);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        labelText.setText("消息");
    }

    private void initViewPager(){
        List<Fragment> list = new ArrayList<>();
        list.add(new MessageFragment());
        list.add(new ContactFragment());
        list.add(new MapFragment());
        adapter = new FragmentAdapter(getSupportFragmentManager(),list);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HeaderInitEvent event){

        Glide.with(this)
                .load(event.getAvatarUrl())
                .error(R.drawable.header_default_icon)
                .into(avatarView);
    }

    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_contact:
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
                break;

        }
        return true;
    }

    @Override
    public void onFinish() {
        initViewPager();
    }

    @Override
    public void onFail(Throwable e) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.contact_text:
                viewPager.setCurrentItem(1);
                break;
            case R.id.message_text:
                viewPager.setCurrentItem(0);
                break;
            case R.id.main_avatar_view:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.map_text:
                viewPager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position==1){
            labelText.setText("联系人");
            bottomLayout.setVisibility(View.VISIBLE);
        }else if (position==0){
            labelText.setText("消息");
            bottomLayout.setVisibility(View.VISIBLE);
        }else if (position==2){
            bottomLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ||ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_PHONE_STATE}, 1);
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {

            }
        } else {
            Toast.makeText(this,"拒绝权限将不能正常使用该功能",Toast.LENGTH_LONG).show();
        }
    }

}
