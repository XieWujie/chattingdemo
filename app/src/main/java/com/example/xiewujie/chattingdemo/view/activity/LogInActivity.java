package com.example.xiewujie.chattingdemo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.sns.SNS;
import com.avos.sns.SNSType;
import com.example.xiewujie.chattingdemo.R;
import com.example.xiewujie.chattingdemo.model.user.Owner;
import com.example.xiewujie.chattingdemo.model.user.OwnerManager;
import com.example.xiewujie.chattingdemo.presenter.LogInPresenter;
import com.example.xiewujie.chattingdemo.view.view.LogInView;

public class LogInActivity extends BaseActivity implements LogInView {

    private LogInPresenter presenter;
    private EditText userNameText;
    private EditText passwordText;
    private Button logInButton;
    private TextView registeredText;
    private TextView qqlogin;
    private  SNSType thirdPartyType;
    private Owner owner;
    private String acount;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        userNameText = $(R.id.log_in_use);
        passwordText = $(R.id.log_in_pass_word);
        logInButton = $(R.id.log_in_button);
        registeredText = $(R.id.to_sign_up);
        qqlogin = $(R.id.qq_login);
        presenter = new LogInPresenter(this,this);
        logInButton.setOnClickListener(this);
        registeredText.setOnClickListener(this);
        qqlogin.setOnClickListener(this);
        owner = OwnerManager.UserManagerHelper.getInstance().getOwner();
        password = owner.getPassword();
        acount = owner.getAccountNumber();
        if (owner.getUserInfo()!=null){
            presenter.logInWithAuthorData(owner.getUserInfo());
            return;
        }
        if (owner!=null){
          String account = owner.getAccountNumber();
          String password = owner.getPassword();
          if (account!=null){
              userNameText.setText(account);
          }
          if (password!=null){
              passwordText.setText(password);
          }
        }
    }

    @Override
    public void onFail(Throwable e) {
        showSnackbar(logInButton,e.getMessage());
    }

    @Override
    public void onFinish() {
        Intent intent = new Intent(this,MainActivity.class);
        if (password!=null&&acount!=null){
            owner.setPassword(password);
            owner.setAccountNumber(acount);
            owner.save();
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.log_in_button:
                acount = userNameText.getText().toString();
                password = passwordText.getText().toString();
                if (TextUtils.isEmpty(acount)){
                    onFail(new Throwable("请输入账号"));
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    onFail(new Throwable("请输入密码"));
                    return;
                }
                presenter.logIn(acount,password);
                break;
            case R.id.to_sign_up:
                Intent intent = new Intent(LogInActivity.this,RegisteredActivity.class);
                startActivity(intent);
                break;
            case R.id.qq_login:
                thirdPartyType = SNSType.AVOSCloudSNSQQ;
                presenter.qqLogin(LogInActivity.this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            SNS.onActivityResult(requestCode, resultCode, data, thirdPartyType);
        }
    }
}
