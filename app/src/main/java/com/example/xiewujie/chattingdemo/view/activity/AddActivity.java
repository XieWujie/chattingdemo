package com.example.xiewujie.chattingdemo.view.activity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.example.xiewujie.chattingdemo.R;
import com.example.xiewujie.chattingdemo.presenter.AddPresenter;
import com.example.xiewujie.chattingdemo.service.MyLineaLayoutManager;
import com.example.xiewujie.chattingdemo.view.adapter.AddAdapter;
import com.example.xiewujie.chattingdemo.view.adapter.ViewPagerAdapter;
import com.example.xiewujie.chattingdemo.view.view.AddView;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends BaseActivity implements ViewPager.OnPageChangeListener,AddView {

    private ViewPagerAdapter vpAdapter;
    private ViewPager viewPager;
    private List<View> mList = new ArrayList<>();
    private List<AVObject> dataList = new ArrayList<>();
    private TextView addTab;
    private LinearLayout.LayoutParams layoutParams;
    private int width;
    private RecyclerView[] recyclerViews = new RecyclerView[2];
    private AddAdapter rcAdapter;
    private EditText[] editTexts = new EditText[2];
    private AddPresenter presenter;
    private TextView contactText;
    private TextView groupText;
    private int leftMargin;
    private LinearLayout[] searchLayouts = new LinearLayout[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        viewPager = $(R.id.add_view_pager);
        addTab = $(R.id.add_tab);
        contactText = $(R.id.add_contact_text);
        groupText = $(R.id.add_group);
        presenter = new AddPresenter(this,this);
        contactText.setOnClickListener(this);
        groupText.setOnClickListener(this);
        Toolbar toolbar= (Toolbar) findViewById(R.id.add_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initView();
    }

    private void initView(){
        layoutParams =(LinearLayout.LayoutParams) addTab.getLayoutParams();
        int tabWidth = layoutParams.width;
        int displayWidth = getWindowManager().getDefaultDisplay().getWidth();
        leftMargin = displayWidth/4-tabWidth/2;
        layoutParams.leftMargin = leftMargin;
        addTab.setLayoutParams(layoutParams);
        width = displayWidth/2;
        rcAdapter = new AddAdapter(dataList);
        addView(0);
        addView(1);
        vpAdapter = new ViewPagerAdapter(mList);
        viewPager.setAdapter(vpAdapter);
        viewPager.setOnPageChangeListener(this);
        rcAdapter.onItemClick(new  AddAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String name) {
                dataList.clear();
             //   int currentItem = viewPager.getCurrentItem();
            //    editTexts[currentItem].setText(name);
            }
        });
    }

    private void addView(final int position){
        View view = LayoutInflater.from(this).inflate(R.layout.seacher_layout,null);
        recyclerViews[position] = (RecyclerView)view.findViewById(R.id.search_recycler_view);
        editTexts[position] = (EditText)view.findViewById(R.id.add_search_edit_text);
        searchLayouts[position] = (LinearLayout)view.findViewById(R.id.add_search_layout);
        MyLineaLayoutManager layoutManager = new MyLineaLayoutManager(this);
        recyclerViews[position].setLayoutManager(layoutManager);
        mList.add(view);
        recyclerViews[position].setAdapter(rcAdapter);
        editTexts[position].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence!=null&&charSequence.length()>0){
                    if (position==0){
                        presenter.getValues(charSequence.toString());
                        editTexts[0].setSelection(editTexts[0].length());
                    }else {
                        presenter.getValues(charSequence.toString());
                        editTexts[1].setSelection(editTexts[1].length());
                    }

                }else {
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editTexts[position].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    searchLayouts[position].setVisibility(View.INVISIBLE);
                }else {
                    searchLayouts[position].setVisibility(View.VISIBLE);
                    editTexts[position].setText("");
                }
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        layoutParams.leftMargin = leftMargin+(int)((position+positionOffset)*width);
        addTab.setLayoutParams(layoutParams);
    }

    @Override
    public void onPageSelected(int position) {
        dataList.clear();
        rcAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onFail(Throwable e) {
        showSnackbar(addTab,e.getMessage());
        e.printStackTrace();
    }

    @Override
    public void onFinish() {
        rcAdapter.notifyDataSetChanged();
    }

    @Override
    public void beforeGetValues() {
        dataList.clear();
        rcAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_group:
                viewPager.setCurrentItem(1);
                break;
            case R.id.add_contact_text:
                viewPager.setCurrentItem(0);
                break;
        }
    }

    @Override
    public void getValues(AVObject object) {
      dataList.add(object);
    }
}
