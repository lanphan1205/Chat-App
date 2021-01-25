package com.example.chatapp;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class AddFriendsActivity extends AppCompatActivity {

    private Context mContext = AddFriendsActivity.this;

    public static final String LOG_CAT = "Logcat";

    private String userId;

    private RecyclerView mRecyclerView;
    private FriendListAdapter mAdapter;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<User> usersTemp = new ArrayList<>();
    private ArrayList<String> userIdList = new ArrayList<>();    // to hold userId of current user's friends

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friends);

        Intent intent = getIntent();
        userId = intent.getStringExtra(ChatListActivity.USER_ID);

        // Set up Tool Bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_add_friends);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        myToolbar.setTitle(getString(R.string.add_friends_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get data
        // Return users that already signed up but not yet friends with local user

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Query all the users except for current user
        db.collection("users").whereNotEqualTo("userId", userId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(LOG_CAT, document.getId() + " => " + document.getData());
                                User user = document.toObject(User.class);
                                usersTemp.add(user);
                            }

                            // Query all users who are friends with local user. Remove these users from users list
                            db.collection("users").whereEqualTo("userId", userId).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d(LOG_CAT, document.getId() + " => " + document.getData());
                                                    User user = document.toObject(User.class);
                                                    userIdList.addAll(user.getFriends());
                                                }
                                                Log.d(LOG_CAT, userIdList.toString());

                                                for(User user: usersTemp) {
                                                    if(!userIdList.contains(user.getUserId())) {
                                                        users.add(user);
                                                        Log.d(LOG_CAT, user.getUserName());
                                                    }
                                                }

                                                // Set up recycler view
                                                mRecyclerView = findViewById(R.id.recyclerViewAddFriends);
                                                mAdapter = new FriendListAdapter(mContext, users);
                                                mRecyclerView.setAdapter(mAdapter);
                                                mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                                            } else {
                                                Log.d(LOG_CAT, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });

                        } else {
                            Log.d(LOG_CAT, "Error getting documents: ", task.getException());
                        }
                    }
                });




//        users.addAll(Arrays.asList(new User("002", "User 3"), new User("003", "User 4")));





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_friends, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public String getUserId() {
        return this.userId;
    }
}
