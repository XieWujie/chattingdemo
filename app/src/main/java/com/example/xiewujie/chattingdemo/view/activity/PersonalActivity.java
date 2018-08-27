package com.example.xiewujie.chattingdemo.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xiewujie.chattingdemo.R;
import com.example.xiewujie.chattingdemo.constant.Conversation;
import com.example.xiewujie.chattingdemo.model.user.ChatUser;
import com.example.xiewujie.chattingdemo.model.user.MyContactProvider;
import com.example.xiewujie.chattingdemo.model.user.Owner;
import com.example.xiewujie.chattingdemo.model.user.OwnerManager;
import com.example.xiewujie.chattingdemo.presenter.PersonalPresenter;
import com.example.xiewujie.chattingdemo.view.view.PersonalView;

import java.util.HashSet;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalActivity extends BaseActivity implements PersonalView {

    private CircleImageView headerImage;
    private ImageView backgroundImage;
    private TextView nickNameText;
    private PersonalPresenter personalPresenter;
    private TextView addToContact;
    private TextView sendMessage;
    private  String userId;
    private TextView ageText;
    private TextView genderText;
    private TextView locationText;
    private LinearLayout cancelLayout;
    private ChatUser chatUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        headerImage = $(R.id.personal_user_header_view);
        backgroundImage = $(R.id.personal_background_image);
        nickNameText = $(R.id.personal_user_nic_name_text);
        addToContact = $(R.id.add_to_friend);
        sendMessage = $(R.id.send_message);
        ageText = $(R.id.personal_age_text);
        locationText = $(R.id.personal_position_text);
        cancelLayout = $(R.id.personal_back_layout);
        genderText = $(R.id.personal_gender_text);
        personalPresenter = new PersonalPresenter(this,this);
        userId = getIntent().getStringExtra("userId");
        personalPresenter.getData(userId);
        addToContact.setOnClickListener(this);
        sendMessage.setOnClickListener(this);
        cancelLayout.setOnClickListener(this);
        HashSet<String> allContactsId = MyContactProvider.ContactProviderHelper.getInstance().getAllIds();
        if (allContactsId.contains(userId)){
            addToContact.setText("已是好友");
            addToContact.setClickable(false);
            addToContact.setBackgroundColor(Color.parseColor("#999999"));
        }
        if (userId.equals(OwnerManager.UserManagerHelper.getInstance().getOwner().getUserId())){
            sendMessage.setText("编辑资料");
        }

    }
    private void initView(final ChatUser chatUser){
        nickNameText.setText(chatUser.getName());
        ageText.setText(String.valueOf(chatUser.getAge()));
        locationText.setText(chatUser.getLocation());
        genderText.setText(chatUser.getGender());
                Glide.with(PersonalActivity.this)
                        .load(chatUser.getBackgroudUrl())
                        .error(R.drawable.personal_default_background)
                        .into(backgroundImage);
                Glide.with(PersonalActivity.this)
                        .load(chatUser.getAvatarUrl())
                        .error(R.drawable.header_default_icon)
                        .into(headerImage);

    }

    @Override
    public void getData(ChatUser user) {
      initView(user);
      chatUser = user;
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onFail(Throwable e) {
        showSnackbar(nickNameText,e.getMessage());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_to_friend:
                Intent addIntent = new Intent(this,ContactVerifyActivity.class);
                if (chatUser==null)return;
                addIntent.putExtra("contact",chatUser);
                startActivity(addIntent);
                break;
            case R.id.send_message:
                if (sendMessage.getText().equals("编辑资料")){
                    Intent intent = new Intent(PersonalActivity.this,EditDataActivity.class);
                    startActivity(intent);
                    return;
                }
                Intent intent = new Intent(PersonalActivity.this, ChatActivity.class);
                intent.putExtra(Conversation.WHAT_CHAT_ID, userId);
                startActivity(intent);
                break;
            case R.id.personal_back_layout:
                finish();
                break;
        }
    }
}
