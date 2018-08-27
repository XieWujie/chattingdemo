package com.example.xiewujie.chattingdemo.presenter;

import android.content.Context;

import com.example.xiewujie.chattingdemo.view.view.BaseView;

public class BasePresenter<T extends BaseView> {
        protected T view;
        protected Context mContext;


    public BasePresenter(Context mContext,T view) {
        this.view = view;
        this.mContext = mContext;
    }

    public void attachView(Context context,T view){
            this.view = view;
            mContext = context;
        }

        public void detachView(){
            this.view = null;
        }

        public T getView(){
            return view;
        }

        boolean isViewAttach(){
            return view!=null;
        }
}
