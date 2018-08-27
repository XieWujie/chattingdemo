package com.example.xiewujie.chattingdemo.view.activity;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.xiewujie.chattingdemo.R;
import com.example.xiewujie.chattingdemo.service.ActivityManager;
import com.example.xiewujie.chattingdemo.service.MyService;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        MyService.setStatusBar(this);
        ActivityManager.addActivity(this);
    }

    protected <T extends View> T $(int id){
        return (T)findViewById(id);
    }

    @Override
    public void onClick(View v) {

    }

    protected void showSnackbar(View v,String content){
        Snackbar.make(v,content,Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.remove(this);
    }
}
