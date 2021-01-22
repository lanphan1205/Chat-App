package com.example.chatapp2;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder>{
    LayoutInflater mInflater;
    ArrayList<Message> data;
    Context context;

    public MessageListAdapter(Context context, ArrayList<Message> data) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View messageView = mInflater.inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(messageView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = data.get(position);
        String msg = message.getMessage();
        holder.textViewMessage.setText(msg);

        ConstraintLayout constraintLayout = holder.constraintLayout;
        LinearLayout linearLayout = holder.linearLayout;
        TextView textViewMessage = holder.textViewMessage;
        Log.d(MainActivity.LOG_CAT, message.getSender());
        if(message.getSender().equals(context.getString(R.string.local_uid))) {
            // align msg right
            Log.d(MainActivity.LOG_CAT, "align right");
            ConstraintSet constraintSet= new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.linearLayout, ConstraintSet.END, R.id.constraintLayout, ConstraintSet.END, 4);
            constraintSet.applyTo(constraintLayout);
            textViewMessage.setBackgroundColor(context.getResources().getColor(R.color.purple_200));
        } else {
            // align msg left
            Log.d(MainActivity.LOG_CAT, "align left");
            ConstraintSet constraintSet= new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.linearLayout, ConstraintSet.START, R.id.constraintLayout, ConstraintSet.START, 4);
            constraintSet.applyTo(constraintLayout);
            textViewMessage.setBackgroundColor(context.getResources().getColor(R.color.teal_200));
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{
        private View messageView;
        private ConstraintLayout constraintLayout;

        // messageLayout
        private LinearLayout linearLayout;
        private TextView textViewMessage;
        public MessageViewHolder(View messageView, MessageListAdapter adapter) {
            super(messageView);
            textViewMessage = messageView.findViewById(R.id.textViewMessage);
            linearLayout = messageView.findViewById(R.id.linearLayout);
            constraintLayout = messageView.findViewById(R.id.constraintLayout);
        }

    }
}
