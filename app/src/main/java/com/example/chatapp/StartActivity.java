package com.example.chatapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class StartActivity extends AppCompatActivity {
    private Context mContext = StartActivity.this;

    private Button buttonEnter;
    private EditText editTextUserId;

    private String userID;
    public static final String USER_ID = "StartActivity.UserId";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        buttonEnter = findViewById(R.id.buttonEnter);
        editTextUserId = findViewById(R.id.editTextUserId);

        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userID = editTextUserId.getText().toString();
                if(userID.equals("000") || userID.equals("001")) {
                    Intent intent = new Intent(mContext, ChatListActivity.class);
                    intent.putExtra(USER_ID, userID);
                    startActivity(intent);
                } else {
                    // TODO Make Toast to notify userID is not found
                }

            }
        });
    }
}
