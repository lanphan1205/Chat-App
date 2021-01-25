package com.example.chatapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChatListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ChatListAdapter mAdapter;
    private Context mContext = ChatListActivity.this;
    private ArrayList<Chat> chatList = new ArrayList<>();
    private String userId;
    private boolean isLogout;

    public static final String LOG_CAT = "Logcat";

    private SharedPreferences mSharedPreferences;
    public static final String PREFERENCE_FILE_KEY = "ChatListActivity.SharedPreferences";
    public static final String USER_ID = "ChatListActivity.UserId";
    public static final String DEFAULT_VALUE = "";
    public static final String CHAT_ID = "ChatListActivity.ChatId";
    public static final String CHAT_NAME = "ChatListActivity.ChatName";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_list);

        // Set up Tool Bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        myToolbar.setTitle(getString(R.string.chat_list_title));

        // Get user ID through SharedPreferences
        mSharedPreferences = mContext.getSharedPreferences(PREFERENCE_FILE_KEY, MODE_PRIVATE);
        userId = mSharedPreferences.getString(USER_ID, DEFAULT_VALUE);


        if(userId.equals(DEFAULT_VALUE)) {
            //TODO Get user Id through Intent passed from Start Activity
            Intent intent = getIntent();
            userId = intent.getStringExtra(StartActivity.USER_ID);
        }

        //Comment the line below when launching this activity from StartActivity
//        userId = "test-user";

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(userId).collection("chatroom_names")
                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Map<String, Object> user = document.getData();
//                        List<String> chatRooms = (List<String>) user.get("chatrooms");
//                        for(String chatroom: chatRooms) {
//                            chatList.add(new Chat(userId, chatroom));
//                        }
////                        chatList.addAll(chatRooms);
////                        Log.d("Logcat", chatList.toString());
//
//                        mRecyclerView = findViewById(R.id.recyclerViewChatList);
//                        mAdapter = new ChatListAdapter(mContext, chatList);
//                        mRecyclerView.setAdapter(mAdapter);
//                        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//
//                    } else {
//                        System.out.println("No such document");
//                    }
//                } else {
//                    System.out.println("get failed with " + task.getException());
//                }
//            }
//        });

        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(LOG_CAT, document.getId() + " => " + document.getData());
                        Chat chat = document.toObject(Chat.class);
                        chatList.add(chat);
                    }

                    mRecyclerView = findViewById(R.id.recyclerViewChatList);
                    mAdapter = new ChatListAdapter(mContext, chatList);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                } else {
                    Log.d(LOG_CAT, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_logout:
                isLogout = true;
                Intent intentLogOut = new Intent(mContext, StartActivity.class);
                startActivity(intentLogOut);
                return true;
            case R.id.action_add_friends:
                //TODO start Add Friends Activity
                Intent intentAddFriends = new Intent(mContext, AddFriendsActivity.class);
                intentAddFriends.putExtra(USER_ID, userId);
                startActivity(intentAddFriends);
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Logcat", "ChatListActivity onPause");
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if(!isLogout) {
            editor.putString(USER_ID, userId);
        } else {
            editor.putString(USER_ID, DEFAULT_VALUE);
        }
        editor.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Logcat", "ChatListActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Logcat", "ChatListActivity onDestroy");
    }
}
