package com.example.chatapp;

import java.util.ArrayList;
import java.util.List;

public class User {

    // TODO Should match the field in the "users" collection
    private String userId;
    private String userName;
    private List<String> friends = new ArrayList<>();

    public User() {

    }

    public User(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;

    }

    public String getUserId() {
        return this.userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public List<String> getFriends() { return this.friends; }

    public void addFriend(String userId) { this.friends.add(userId); }

}
