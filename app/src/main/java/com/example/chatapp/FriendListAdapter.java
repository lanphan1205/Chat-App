package com.example.chatapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendViewHolder> {
    Context context;
    private LayoutInflater mInflater;
    private ArrayList<User> data;

    public FriendListAdapter(Context context, ArrayList<User> data) {
        mInflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.friend_item, parent, false);
        return new FriendViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        User user = data.get(position);
        String userName = user.getUserName();
        holder.textViewUserName.setText(userName);
        holder.remoteUser = user;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {
        User remoteUser;
        User localUser;
        TextView textViewUserName;
        Button buttonAddFriends;

        public FriendViewHolder(View itemView, FriendListAdapter adapter) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textViewChatName);
            buttonAddFriends = itemView.findViewById(R.id.buttonAddFriends);
            buttonAddFriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO Handle onClick Event
                    // TODO disable the button after 1 click
                    buttonAddFriends.setEnabled(false);
                    // TODO Update button UI
                    buttonAddFriends.setBackgroundColor(context.getResources().getColor(R.color.grey_out));

                    // Current userId
                    String userIdLocal = ((AddFriendsActivity) context).getUserId();
                    Log.d(ChatListActivity.LOG_CAT, "from chatlistadapter" + userIdLocal);

                    // Remote userId
                    String userIdRemote = remoteUser.getUserId();
                    String userNameRemote = remoteUser.getUserName();

                    // Generate document Id for new chat room
                    String randomString = Utils.generateRandomString();

                    //Prepare the user object
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Possible data racing

                    // Query current user document from database and update the user's friends list

                    db.collection("users").whereEqualTo("userId", userIdLocal)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                 String documentId = "null";
                                                                 if (task.isSuccessful()) {
                                                                     for (QueryDocumentSnapshot document : task.getResult()) {
                                                                         documentId = document.getId();
                                                                         Log.d(AddFriendsActivity.LOG_CAT, documentId + " => " + document.getData());

                                                                         localUser = document.toObject(User.class);
                                                                         localUser.addFriend(userIdRemote);
                                                                     }

                                                                     // Update friend list
                                                                     db.collection("users").document(documentId).set(localUser);



                                                                     // Create a new document in "chatrooms" collection
                                                                     db.collection("chatrooms").document(randomString).set(new HashMap<>());

                                                                     // Create a new document in "chatroom_names" sub-collection of the current user document
                                                                     db.collection("users").document(documentId).collection("chatroom_names")
                                                                             .add(new Chat(userIdLocal, randomString, userNameRemote));

                                                                     // Query the remote user document and update the user's friend list
                                                                     db.collection("users").whereEqualTo("userId", userIdRemote)
                                                                             .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                              @Override
                                                                                                              public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                                  String documentId = "null";
                                                                                                                  if (task.isSuccessful()) {
                                                                                                                      for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                                                          documentId = document.getId();
                                                                                                                          Log.d(AddFriendsActivity.LOG_CAT, documentId + " => " + document.getData());

                                                                                                                          remoteUser = document.toObject(User.class);
                                                                                                                          remoteUser.addFriend(userIdLocal);
                                                                                                                      }

                                                                                                                      // Update friend list
                                                                                                                      db.collection("users").document(documentId).set(remoteUser);


                                                                                                                      // Create a new document in "chatroom_names" sub-collection of the current user document
                                                                                                                      db.collection("users").document(documentId).collection("chatroom_names")
                                                                                                                              .add(new Chat(userIdRemote, randomString, localUser.getUserName()));

                                                                                                                  }
                                                                                                              }
                                                                                                          }
                                                                     );


                                                                 }
                                                             }
                                                         }
                    );



                }
            });
        }
    }
}
