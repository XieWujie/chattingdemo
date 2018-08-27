package com.example.xiewujie.chattingdemo.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xiewujie.chattingdemo.R;
import com.example.xiewujie.chattingdemo.model.user.MyContactProvider;
import com.example.xiewujie.chattingdemo.view.adapter.ContactAdapter;
import com.example.xiewujie.chattingdemo.view.event.AddContactEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ContactFragment extends Fragment {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.main_contact_layout,container,false);
       refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.contact_refresh_layout);
       recyclerView = (RecyclerView)view.findViewById(R.id.main_contact_rc_view);
        adapter = new ContactAdapter();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                freshContact();
            }
        });
        EventBus.getDefault().register(this);
       return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        freshContact();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AddContactEvent event){
        freshContact();
    }

    private void freshContact(){
        adapter.setContactList(MyContactProvider.ContactProviderHelper.getInstance().getAllUsers());
        adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }
}
