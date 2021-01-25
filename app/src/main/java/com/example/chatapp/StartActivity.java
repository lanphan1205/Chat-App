package com.example.chatapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class StartActivity extends AppCompatActivity {
    private Context mContext = StartActivity.this;

    private Button buttonEnter;
    private EditText editTextUserId;

    private String userID;
    public static final String USER_ID = "StartActivity.UserId";

    private ArrayList<String> userIdList = new ArrayList<>();

    public static final String LOG_CAT = "LogCat";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        buttonEnter = findViewById(R.id.buttonEnter);
        editTextUserId = findViewById(R.id.editTextUserId);

        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Button disabled and greyed out
                buttonEnter.setEnabled(false);
                buttonEnter.setBackgroundColor(getResources().getColor(R.color.grey_out));

                userID = editTextUserId.getText().toString();

                // Conduct check for userId
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // button return color
                            buttonEnter.setBackgroundColor(getResources().getColor(R.color.purple_500));

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(LOG_CAT, document.getId() + " => " + document.getData());
                                User user = document.toObject(User.class);
                                userIdList.add(user.getUserId());
                            }

                            if(userIdList.contains(userID)) {
                                Intent intent = new Intent(mContext, ChatListActivity.class);
                                intent.putExtra(USER_ID, userID);
                                startActivity(intent);
                            } else {

                                buttonEnter.setEnabled(true);
                                buttonEnter.setBackgroundColor(getResources().getColor(R.color.purple_500));

                                // TODO Make Toast to notify userID is not found
                                String text = "User Id you enter does not exist." +
                                        "\nSign up or log in with a different User Id";
                                Toast toast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
                                toast.show();
                            }
                        } else {
                            Log.d(LOG_CAT, "Error getting documents: ", task.getException());
                        }
                    }
                });




            }
        });
    }
}
