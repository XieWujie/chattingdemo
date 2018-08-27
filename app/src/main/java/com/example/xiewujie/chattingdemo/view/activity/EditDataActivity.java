package com.example.xiewujie.chattingdemo.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.xiewujie.chattingdemo.R;
import com.example.xiewujie.chattingdemo.model.user.Owner;
import com.example.xiewujie.chattingdemo.model.user.OwnerManager;

public class EditDataActivity extends BaseActivity {

    private EditText nicNameText;
    private Spinner genderSpinner;
    private EditText locationText;
    private EditText ageText;
    private TextView backText;
    private ImageView backView;
    private TextView ensureText;
    private int selectPosition;
    private  ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        nicNameText = $(R.id.nick_name_edit);
        genderSpinner = $(R.id.gender_choose);
        locationText = $(R.id.home_choose);
        backText = $(R.id.edit_back_text);
        backView = $(R.id.edit_back_view);
        ensureText = $(R.id.edit_ensure);
        ageText = $(R.id.age_edit);
        ensureText.setOnClickListener(this);
        backView.setOnClickListener(this);
        backText.setOnClickListener(this);
        initView();
    }
    private void initView(){
        Owner owner = OwnerManager.UserManagerHelper.getInstance().getOwner();
        nicNameText.setText(owner.getName());
        ageText.setText(String.valueOf(owner.getAge()));
        locationText.setText(owner.getLocation());
        adapter =ArrayAdapter.createFromResource(this,R.array.gender,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_back_text:
            case R.id.edit_back_view:
                finish();
            case R.id.edit_ensure:
                initData();
                showSnackbar(nicNameText,"修改成功");
                finish();
        }
    }

    private void initData(){

        String nickName = nicNameText.getText().toString();
        String location = locationText.getText().toString();
        String gender = adapter.getItem(selectPosition).toString();
        int age = Integer.valueOf(ageText.getText().toString());
        OwnerManager.UserManagerHelper.getInstance().updateOwner(new String[]{"username","location","gender"},
                new String[]{nickName,location,gender});
        OwnerManager.UserManagerHelper.getInstance().updateOwner(new String[]{"age"},new Integer[]{age});
        Owner owner = OwnerManager.UserManagerHelper.getInstance().getOwner();
        owner.setName(nickName);
        owner.setLocation(location);
        owner.setGender(gender);
        owner.setAge(age);
        owner.save();

    }
}
