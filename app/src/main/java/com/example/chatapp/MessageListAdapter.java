package com.example.chatapp;

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
        setHasStableIds(true);
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

        String timeStamp = message.getFormattedTimeStamp();
        holder.textViewDate.setText(timeStamp);

        ConstraintLayout constraintLayout = holder.constraintLayout;
        LinearLayout linearLayoutMessage = holder.linearLayoutMessage;
        TextView textViewMessage = holder.textViewMessage;
        LinearLayout linearLayoutDate = holder.linearLayoutDate;

        Log.d(MainActivity.LOG_CAT, message.getSender());
        if(message.getSender().equals(((ChatMessagesActivity) context).getUserId())) {
            // align msg right
            Log.d(MainActivity.LOG_CAT, "align right");
            ConstraintSet constraintSet= new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.linearLayoutMessage, ConstraintSet.END, R.id.constraintLayout, ConstraintSet.END, 4);
//            constraintSet.applyTo(constraintLayout);
            textViewMessage.setBackgroundColor(context.getResources().getColor(R.color.purple_200));

            // Add Text View to the left of the message
            constraintSet.connect(R.id.linearLayoutDate, ConstraintSet.END, R.id.linearLayoutMessage, ConstraintSet.START,4);
            constraintSet.applyTo(constraintLayout);
        } else {
            // align msg left
            Log.d(MainActivity.LOG_CAT, "align left");
            ConstraintSet constraintSet= new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.linearLayoutMessage, ConstraintSet.START, R.id.constraintLayout, ConstraintSet.START, 4);
//            constraintSet.applyTo(constraintLayout);
            textViewMessage.setBackgroundColor(context.getResources().getColor(R.color.teal_200));

            // Add Text View to the right of the message
            constraintSet.connect(R.id.linearLayoutDate, ConstraintSet.START, R.id.linearLayoutMessage, ConstraintSet.END,4);
            constraintSet.applyTo(constraintLayout);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{
        private View messageView;
        private ConstraintLayout constraintLayout;

        // messageLayout
        private LinearLayout linearLayoutMessage;
        private LinearLayout linearLayoutDate;
        private TextView textViewMessage;
        private TextView textViewDate;
        public MessageViewHolder(View messageView, MessageListAdapter adapter) {
            super(messageView);
            textViewDate = messageView.findViewById(R.id.textViewDate);
            textViewMessage = messageView.findViewById(R.id.textViewMessage);
            linearLayoutMessage = messageView.findViewById(R.id.linearLayoutMessage);
            linearLayoutDate = messageView.findViewById(R.id.linearLayoutDate);
            constraintLayout = messageView.findViewById(R.id.constraintLayout);


        }

    }
}
