package com.example.xiewujie.chattingdemo.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.example.xiewujie.chattingdemo.R;
import com.example.xiewujie.chattingdemo.view.activity.PersonalActivity;

import java.util.List;

public class AddAdapter extends RecyclerView.Adapter<AddAdapter.ViewHolder> {


    private List<AVObject> mList;
    private Context mContext;
    private OnItemClickListener listener;

    public AddAdapter(List<AVObject> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.only_text,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      final AVObject object = mList.get(position);
      String username = object.getString("username");
      holder.autoText.setText(username);
      holder.autoText.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(mContext, PersonalActivity.class);
              String id = object.getString("userId");
              intent.putExtra("userId", id);
              mContext.startActivity(intent);
          }
      });
   //   listener.onItemClick(content);

    }

    public void onItemClick(OnItemClickListener listener){
        if (this.listener==null){
            this.listener = listener;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView autoText;
        public ViewHolder(View itemView) {
            super(itemView);
            autoText = (TextView)itemView.findViewById(R.id.add_search_rc_item);
        }
    }


    public interface OnItemClickListener{
        void onItemClick(String name);
    }
}

