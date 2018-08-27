package com.example.xiewujie.chattingdemo.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.bumptech.glide.Glide;
import com.example.xiewujie.chattingdemo.R;
import com.example.xiewujie.chattingdemo.constant.Conversation;
import com.example.xiewujie.chattingdemo.model.user.ChatUser;
import com.example.xiewujie.chattingdemo.model.user.GetUserListener;
import com.example.xiewujie.chattingdemo.model.user.MyContactProvider;
import com.example.xiewujie.chattingdemo.model.user.OwnerManager;
import com.example.xiewujie.chattingdemo.service.MyService;
import com.example.xiewujie.chattingdemo.view.activity.ChatActivity;

import java.util.ArrayList;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder>{

    private List<AVIMConversation> mList = new ArrayList<>();
    private Context mContext;

    public MessageListAdapter() {
        super();
    }

    public List<AVIMConversation> getDataList() {
        return mList;
    }

    public void setDataList(List<AVIMConversation> datas) {
        mList.clear();
        if (mList!=null){
          for (AVIMConversation conversation:datas){
              if (!(conversation.getLastMessage() instanceof AVIMLocationMessage
                      ||conversation.getLastMessage().getFrom().equals(OwnerManager.UserManagerHelper.getInstance().getOwner().getUserId()))){
                  mList.add(conversation);
              }
          }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.message_list_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AVIMConversation conversation = mList.get(position);
        final AVIMMessage typedMessage = conversation.getLastMessage();
        if (typedMessage==null)return;
        final String id = typedMessage.getFrom();
        MyContactProvider.ContactProviderHelper.getInstance().getUserById(id, new GetUserListener() {
            @Override
            public void done(ChatUser chatUser, Exception e) {
                if (e==null){
                    initView(conversation,holder,chatUser,typedMessage);
                }
            }
        });
    }

    private void initView(final AVIMConversation conversation, ViewHolder holder, final ChatUser user, AVIMMessage typedMessage){
        String remarkName = user.getRemarkName();
        if (remarkName==null||remarkName.length()<1){
            holder.nameView.setText(user.getName());
        }else {
            holder.nameView.setText(remarkName);
        }
        Glide.with(mContext).load(user.getAvatarUrl()).error(R.drawable.lcim_default_avatar_icon).into(holder.avatarView);
        holder.timeView.setText(MyService.showTime(typedMessage.getTimestamp()));
        if (typedMessage instanceof AVIMTextMessage){
            AVIMTextMessage message = (AVIMTextMessage)typedMessage;
            if (!TextUtils.isEmpty(message.getContent())){
                holder.contentView.setText(message.getText());
            }
        }
        int count = conversation.getUnreadMessagesCount();
        if (count!=0){
            holder.messageCount.setText(String.valueOf(count));
            holder.messageCount.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra(Conversation.WHAT_CHAT_ID,conversation.getConversationId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

       private ImageView avatarView;
       private TextView messageCount;
       private TextView timeView;
       private TextView nameView;
       private TextView contentView;
       private LinearLayout itemLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            initView();
        }

        private void initView() {
            avatarView = (ImageView) itemView.findViewById(R.id.conversation_item_iv_avatar);
            nameView = (TextView) itemView.findViewById(R.id.conversation_item_name);
            timeView = (TextView) itemView.findViewById(R.id.conversation_item_message_time);
            messageCount = (TextView) itemView.findViewById(R.id.conversation_item_message_count);
            contentView = (TextView) itemView.findViewById(R.id.conversation_item_content);
            itemLayout = (LinearLayout)itemView.findViewById(R.id.conversation_item_layout);
        }
    }
}
