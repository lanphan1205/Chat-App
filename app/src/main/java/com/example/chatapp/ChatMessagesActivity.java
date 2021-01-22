package com.example.chatapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmChannel;
import io.agora.rtm.RtmChannelAttribute;
import io.agora.rtm.RtmChannelListener;
import io.agora.rtm.RtmChannelMember;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmFileMessage;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMediaOperationProgress;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.SendMessageOptions;

public class ChatMessagesActivity extends AppCompatActivity {
    // Context
    private Context mContext = ChatMessagesActivity.this;

    // UI
    private Button buttonSendMessage;
    private EditText editTextMessageMultiLine;

    // RecyclerView & Adapter
    private RecyclerView mRecyclerView;
    private MessageListAdapter mAdapter;

    // Data
    private ArrayList<Message> messageList = new ArrayList<>();
    private String userId;
    private String chatId;

    // Rtm Sdk
    private RtmClient mRtmClient;

    private RtmClientListener mRtmClientListener = new RtmClientListener() {
        @Override
        public void onConnectionStateChanged(int state, int reason) {
            Log.d(LOG_CAT, "Connection state changes to " + state + " reason: " + reason);
        }

        @Override
        public void onMessageReceived(RtmMessage rtmMessage, String peerId) {
            String msg = rtmMessage.getText();
            Log.d(LOG_CAT, "Message received " + "from " + peerId + ": "+ msg);

        }

        @Override
        public void onImageMessageReceivedFromPeer(RtmImageMessage rtmImageMessage, String s) {

        }

        @Override
        public void onFileMessageReceivedFromPeer(RtmFileMessage rtmFileMessage, String s) {

        }

        @Override
        public void onMediaUploadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

        }

        @Override
        public void onMediaDownloadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

        }

        @Override
        public void onTokenExpired() {

        }

        @Override
        public void onPeersOnlineStatusChanged(Map<String, Integer> map) {

        }

    };
    private RtmChannelListener mRtmChannelListener = new RtmChannelListener() {
        @Override
        public void onMemberCountUpdated(int i) {
            Log.i(LOG_CAT, "Member count: " + i);
        }

        @Override
        public void onAttributesUpdated(List<RtmChannelAttribute> list) {

        }

        @Override
        public void onMessageReceived(RtmMessage rtmMessage, RtmChannelMember fromMember) {
            String msg = rtmMessage.getText();
            String fromMemberUserId = fromMember.getUserId();
            Log.d(LOG_CAT, "Message received from Channel Member Id "  +"(" + fromMemberUserId + ")" + ": " + msg);
            Message message = new Message(fromMemberUserId, msg, new Timestamp(System.currentTimeMillis()));
            messageList.add(message);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });

//                    Log.d(LOG_CAT, messageList.toString());


            //TODO Prepare Message Object

            // TODO Get the chatroom Id here: WF1ZEndb2NR9ifxCqRI0
            // TODO Write the this document
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("chatrooms").document("WF1ZEndb2NR9ifxCqRI0").collection("messages")
                    .add(message)
                    .addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Log.d(LOG_CAT, "DocumentSnapshot successfully written!" + " Result: " + o.toString());
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(LOG_CAT, "Error writing document", e);
                        }
                    });
        }
        @Override
        public void onImageMessageReceived(RtmImageMessage rtmImageMessage, RtmChannelMember rtmChannelMember) {

        }

        @Override
        public void onFileMessageReceived(RtmFileMessage rtmFileMessage, RtmChannelMember rtmChannelMember) {

        }

        @Override
        public void onMemberJoined(RtmChannelMember rtmChannelMember) {
            Log.d(LOG_CAT, "Member with uid: " + rtmChannelMember.getUserId() + " join channel: " + rtmChannelMember.getChannelId());
        }

        @Override
        public void onMemberLeft(RtmChannelMember rtmChannelMember) {
            Log.d(LOG_CAT, "Member with uid: " + rtmChannelMember.getUserId() + " leave channel: " + rtmChannelMember.getChannelId());

        }
    };

    private RtmChannel mRtmChannel;

    private static final String APP_ID = "d6bb57ca85614d6d8e2f48a6fa6eba6c";

    public static final String LOG_CAT = "chatapp.Logcat";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_list);

        // Data
//        String[] msgs = {"Hi \n there!", "How \n are \n you?", "It's \n been \n a while..", "How \n is \n Max?",
//        "I have been \n really busy \n  these days \n so \n it's so hard \n to catch up", "I have been \n growing a start up \n that can really \n be something.."};
//        messageList.addAll(Arrays.asList(msgs));
        Intent intent = getIntent();
        userId = intent.getStringExtra(ChatListActivity.USER_ID);
        chatId = intent.getStringExtra(ChatListActivity.CHAT_ID);
        Log.d(LOG_CAT, userId);
        Log.d(LOG_CAT, chatId);

        // Read Data from Database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("chatrooms").document(chatId).collection("messages")
                .orderBy("timeStamp", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(LOG_CAT, "Getting documents successful");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Message message = document.toObject(Message.class);
                                messageList.add(message);
                            }

                            // Set up Recycler View & set Adapter

                            mRecyclerView = findViewById(R.id.recyclerViewMessageOpen);
                            mAdapter = new MessageListAdapter(mContext, messageList);
                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        } else {
                            Log.d(LOG_CAT, "Error getting documents: ", task.getException());
                        }
                    }
                });



        // Start Rtm
        init();
        mRtmClient.login(null, userId, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(LOG_CAT, "Log in Success!");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.d(LOG_CAT, "Log in Failure!");
            }
        });

        createChannel();

        joinChannel();


        // Other UI
        editTextMessageMultiLine = findViewById(R.id.editTextMessageMultiLine);
        buttonSendMessage = findViewById(R.id.buttonSendMessage);
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String msg = editTextMessageMultiLine.getText().toString();
                Log.d(LOG_CAT, "Send Button clicked. Message: " + msg);
                sendChannelMessage(msg);

            }
        });

    }

    public void init() {
        try {
            mRtmClient = RtmClient.createInstance(mContext, APP_ID, mRtmClientListener);
        } catch (Exception e) {
            Log.d(LOG_CAT, "RTM SDK initialization fails");
            throw new RuntimeException("Need to check RTM initialization process");
        }

    }

    private void sendPeerMessage(String peerId, String content) {
        final RtmMessage message = mRtmClient.createMessage();
        message.setText(content);

        SendMessageOptions option = new SendMessageOptions();
        option.enableOfflineMessaging = true;

        mRtmClient.sendMessageToPeer(peerId, message, option, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(LOG_CAT, "Send Message Success");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {

            }
        });

    }

    public void createChannel() {
        try {
            mRtmChannel = mRtmClient.createChannel("Test Channel 2", mRtmChannelListener);
        } catch (RuntimeException e) {
            Log.d(LOG_CAT, "Fails to create channel. Can be due to channel Id is invalid or already in use");
        }

    }

    public void joinChannel() {
        if (mRtmChannel != null) {
            mRtmChannel.join(new ResultCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(LOG_CAT, "Join Channel Success");
                }

                @Override
                public void onFailure(ErrorInfo errorInfo) {
                    Log.d(LOG_CAT, "Join Channel Failure. ErrorCode = " + errorInfo.getErrorCode());
                }
            });

        }
    }

    public void sendChannelMessage(String msg) {
        RtmMessage message = mRtmClient.createMessage();
        message.setText(msg);

        mRtmChannel.sendMessage(message, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(LOG_CAT, "Send Message Channel Success. Message: " + msg);
                Message message = new Message(userId, msg, new Timestamp(System.currentTimeMillis()));
                messageList.add(message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        editTextMessageMultiLine.setText("");
                    }
                });


//                        Log.d(LOG_CAT, messageList.toString());

                //TODO Prepare Message Object

                // TODO Get the chatroom Id here: WF1ZEndb2NR9ifxCqRI0
                // TODO Write the this document
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("chatrooms").document("WF1ZEndb2NR9ifxCqRI0").collection("messages")
                        .add(message)
                        .addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Log.d(LOG_CAT, "DocumentSnapshot successfully written!" + " Result: " + o.toString());
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            Log.w(LOG_CAT, "Error writing document", e);
                            }
                        });
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.d(LOG_CAT, "Send Message Channel Failure. ErrorCode: " + errorInfo.getErrorCode());
            }
        });
    }

    public void leaveChannel() {
        if (mRtmChannel != null) {
            mRtmChannel.leave(new ResultCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(LOG_CAT, "Leave Channel Success");
                }

                @Override
                public void onFailure(ErrorInfo errorInfo) {
                    Log.d(LOG_CAT, "Leave Channel Failure. ErrorCode: " + errorInfo.getErrorCode());
                }
            });
        }
    }

    public String getUserId() { return this.userId; }

    public String getChatId() { return this.chatId; }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_CAT, "onDestroy");
    }
}
