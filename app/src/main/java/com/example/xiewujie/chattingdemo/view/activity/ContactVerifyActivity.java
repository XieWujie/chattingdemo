package com.example.xiewujie.chattingdemo.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xiewujie.chattingdemo.R;
import com.example.xiewujie.chattingdemo.model.user.ChatUser;
import com.example.xiewujie.chattingdemo.model.user.MyContactProvider;
import com.example.xiewujie.chattingdemo.model.convert.Contact;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactVerifyActivity extends BaseActivity {

    private TextView cancelText;
    private TextView sendText;
    private CircleImageView userHeader;
    private TextView nicNameText;
    private TextView sexText;
    private TextView ageText;
    private EditText verifyText;
    private EditText remarkText;
    private ChatUser contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_verify);
         contact = (ChatUser) getIntent().getSerializableExtra("contact");
        initView();
    }

    private void initView(){
        cancelText = $(R.id.add_cancel);
        sendText = $(R.id.add_send);
        userHeader = $(R.id.add_user_header);
        nicNameText = $(R.id.add_nick_name);
        sexText = $(R.id.add_sex);
        ageText = $(R.id.add_age);
        verifyText = $(R.id.add_send_text);
        remarkText = $(R.id.add_remark);
        sendText.setOnClickListener(this);
        nicNameText.setText(contact.getName());
        cancelText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_send:
                MyContactProvider.ContactProviderHelper.getInstance().addContact(contact.getUserId(),remarkText.getText().toString());
                showSnackbar(v,"发送成功");
                final Dialog dialog = new Dialog(this);
                dialog.setTitle("发送成功");
                dialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ContactVerifyActivity.this,MainActivity.class);
                        dialog.dismiss();
                        startActivity(intent);
                    }
                },1000);
                break;
            case R.id.add_cancel:
                finish();
        }
    }
}
