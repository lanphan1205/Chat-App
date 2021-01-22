package com.example.chatapp;

public class Chat {
    private String userID;
    private String chatId;

    public Chat(String userId, String chatId) {
        this.userID = userId;
        this.chatId = chatId;
    }

    public String getUserId() { return this.userID; }
    public String getChatId() {
        return this.chatId;
    }
}
