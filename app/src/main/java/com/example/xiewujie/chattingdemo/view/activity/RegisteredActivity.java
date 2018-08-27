package com.example.xiewujie.chattingdemo.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.xiewujie.chattingdemo.R;
import com.example.xiewujie.chattingdemo.presenter.SignUpPresenter;
import com.example.xiewujie.chattingdemo.view.view.RegisteredView;

public class RegisteredActivity extends BaseActivity implements RegisteredView {

    private EditText userText;
    private EditText passwordFirstText;
    private EditText passwordSecondText;
    private EditText emailText;
    private Button signUpButton;
    private SignUpPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
        userText = $(R.id.registered_user);
        passwordFirstText = $(R.id.registered_pass_word_first);
        passwordSecondText= $(R.id.registered_pass_word_second);
        signUpButton = $(R.id.registered_button);
        emailText = $(R.id.registered_email);
        presenter = new SignUpPresenter(this,this);
        signUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registered_button:
                String firstPassword = passwordFirstText.getText().toString();
                String secondPassword = passwordSecondText.getText().toString();
                if (firstPassword.equals(secondPassword)) {
                    presenter.signUp(userText.getText().toString(), firstPassword, emailText.getText().toString());
                }else {
                    showSnackbar(signUpButton,"两次密码不一致");
                }
        }
    }

    @Override
    public void onFinish() {
        showSnackbar(signUpButton,"注册成功");
        Intent intent = new Intent(RegisteredActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFail(Throwable e) {
        showSnackbar(signUpButton,e.getMessage());
    }
}
