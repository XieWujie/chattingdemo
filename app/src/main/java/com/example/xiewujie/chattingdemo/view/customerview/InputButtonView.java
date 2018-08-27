package com.example.xiewujie.chattingdemo.view.customerview;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.xiewujie.chattingdemo.R;
import com.example.xiewujie.chattingdemo.util.LCIMSoftInputUtils;
import com.example.xiewujie.chattingdemo.view.event.BarRecordEvent;
import com.example.xiewujie.chattingdemo.view.event.BarTextEvent;
import com.example.xiewujie.chattingdemo.view.event.CameraEvent;
import com.example.xiewujie.chattingdemo.view.event.PictureEvent;

import org.greenrobot.eventbus.EventBus;

import static com.example.xiewujie.chattingdemo.util.LCIMSoftInputUtils.showSoftInput;

public class InputButtonView extends LinearLayout{


    private View actionBtn;
    private EditText contentEditText;
    private View sendTextBtn;
    private View keyboardBtn;
    private View moreLayout;
    private LinearLayout actionLayout;
    private View cameraBtn;
    private View pictureBtn;
    private final int MIN_INTERVAL_SEND_MESSAGE = 1000;

    public InputButtonView(Context context) {
        super(context);
        initView(context);
    }

    public InputButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void hideMoreLayout() {
        moreLayout.setVisibility(View.GONE);
    }


    private void initView(Context context) {
        View.inflate(context, R.layout.lcim_chat_input_bottom_bar_layout, this);
        actionBtn = findViewById(R.id.input_bar_btn_action);
        contentEditText = (EditText) findViewById(R.id.input_bar_et_content);
        sendTextBtn = findViewById(R.id.input_bar_btn_send_text);
        keyboardBtn = findViewById(R.id.input_bar_btn_keyboard);
        moreLayout = findViewById(R.id.input_bar_layout_more);
        actionLayout = (LinearLayout) findViewById(R.id.input_bar_layout_action);
        cameraBtn = findViewById(R.id.input_bar_btn_camera);
        pictureBtn = findViewById(R.id.input_bar_btn_picture);
        setEditTextChangeListener();
        actionBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean showActionView =
                        (GONE == moreLayout.getVisibility() || GONE == actionLayout.getVisibility());
                moreLayout.setVisibility(showActionView ? VISIBLE : GONE);
                actionLayout.setVisibility(showActionView ? VISIBLE : GONE);
                LCIMSoftInputUtils.hideSoftInput(getContext(), contentEditText);
            }
        });

        contentEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                moreLayout.setVisibility(View.GONE);
             showSoftInput(getContext(), contentEditText);
            }
        });

        keyboardBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showTextLayout();
            }
        });

        sendTextBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = contentEditText.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(getContext(), R.string.lcim_message_is_null, Toast.LENGTH_SHORT).show();
                    return;
                }

                contentEditText.setText("");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendTextBtn.setEnabled(true);
                    }
                }, MIN_INTERVAL_SEND_MESSAGE);

                EventBus.getDefault().post(new BarTextEvent(content));
            }
        });

        pictureBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new PictureEvent());
            }
        });

        cameraBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new CameraEvent());
            }
        });
    }

    public void addActionView(View view) {
        actionLayout.addView(view);
    }

    private void showTextLayout() {
        contentEditText.setVisibility(View.VISIBLE);
        sendTextBtn.setVisibility(VISIBLE);
        keyboardBtn.setVisibility(View.GONE);
        moreLayout.setVisibility(View.GONE);
        contentEditText.requestFocus();
        LCIMSoftInputUtils.showSoftInput(getContext(), contentEditText);
    }

    private void setEditTextChangeListener() {
        contentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                boolean showSend = charSequence.length() > 0;
                keyboardBtn.setVisibility(!showSend ? View.VISIBLE : GONE);
                sendTextBtn.setVisibility(showSend ? View.VISIBLE : GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}
