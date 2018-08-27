package com.example.xiewujie.chattingdemo.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.xiewujie.chattingdemo.R;
import com.example.xiewujie.chattingdemo.constant.Conversation;
import com.example.xiewujie.chattingdemo.model.user.ChatUser;
import com.example.xiewujie.chattingdemo.view.activity.ChatActivity;
import java.util.HashSet;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private HashSet<ChatUser> mSet = new HashSet<>();
    private Context mContext;
    private Iterator<ChatUser> iterable;

    public void setContactList(HashSet<ChatUser> list){
       mSet.clear();
        if (list!=null){
            mSet.addAll(list);
        }
        iterable = mSet.iterator();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.main_contact_rc_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (!iterable.hasNext()){
            return;
        }
        final ChatUser chatUser = iterable.next();
        if (chatUser.getRemarkName()!=null&&chatUser.getRemarkName().length()>0){
            holder.userNameText.setText(chatUser.getRemarkName());
        }else {
            holder.userNameText.setText(chatUser.getName());
        }
        Glide.with(mContext).load(chatUser.getAvatarUrl()).centerCrop().error(R.drawable.lcim_default_avatar_icon).into(holder.avatarView);
        holder.contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra(Conversation.WHAT_CHAT_ID,chatUser.getUserId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView userNameText;
        private CircleImageView avatarView;
        private LinearLayout contactLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            userNameText = (TextView)itemView.findViewById(R.id.contact_rc_user_name);
            avatarView = (CircleImageView)itemView.findViewById(R.id.contact_rc_user_image);
            contactLayout = (LinearLayout) itemView.findViewById(R.id.contact_item_layout);
        }
    }
}
