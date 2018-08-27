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
import com.example.xiewujie.chattingdemo.view.event.MapTextEvent;

import org.greenrobot.eventbus.EventBus;

import static com.example.xiewujie.chattingdemo.util.LCIMSoftInputUtils.showSoftInput;

public class MapInputBottomView extends LinearLayout {


    private EditText contentEditText;
    private View sendTextBtn;
    private View keyboardBtn;
    private LinearLayout actionLayout;
    private final int MIN_INTERVAL_SEND_MESSAGE = 1000;

    public MapInputBottomView(Context context) {
        super(context);
        initView(context);
    }

    public MapInputBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.map_input_bottom_layout, this);
        contentEditText = (EditText) findViewById(R.id.input_bar_et_content);
        sendTextBtn = findViewById(R.id.input_bar_btn_send_text);
        keyboardBtn = findViewById(R.id.input_bar_btn_keyboard);
        actionLayout = (LinearLayout) findViewById(R.id.input_bar_layout_action);
        setEditTextChangeListener();

        contentEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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

                EventBus.getDefault().post(new MapTextEvent(content));
            }
        });

    }

    private void showTextLayout() {
        contentEditText.setVisibility(View.VISIBLE);
        sendTextBtn.setVisibility(VISIBLE);
        keyboardBtn.setVisibility(View.GONE);
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
