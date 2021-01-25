package com.example.chatapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>{

    private LayoutInflater mInflater;
    private ArrayList<Chat> data;
    private Context context;

    public ChatListAdapter(Context context, ArrayList<Chat> data) {
        mInflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatView = mInflater.inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(chatView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = data.get(position);
        String chatName = chat.getChatName();
        holder.textViewChatName.setText(chatName);
        holder.mChat = chat;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder{
        Chat mChat;
        TextView textViewChatName;
        public ChatViewHolder(View chatView, ChatListAdapter adapter) {
            super(chatView);
            textViewChatName = chatView.findViewById(R.id.textViewChatName);

            chatView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatMessagesActivity.class);
                    intent.putExtra(ChatListActivity.USER_ID, mChat.getUserId());
                    intent.putExtra(ChatListActivity.CHAT_ID, mChat.getChatId());
                    intent.putExtra(ChatListActivity.CHAT_NAME, mChat.getChatName());
                    context.startActivity(intent);
                }
            });
        }
    }
}
