package com.example.chatapp;

public class Chat {

    private String userId;
    private String chatId;
    private String chatName;

    // pass in the userId of the one view the chat (local user)
    // this is how the chat is viewed in the perspective of local user
    // To differentiate between local user and other users in the room
    public Chat() {

    }

    public Chat(String userId, String chatId, String chatName) {
        this.userId = userId;
        this.chatId = chatId;
        this.chatName = chatName;
    }

    public String getUserId() { return this.userId; }
    public String getChatId() {
        return this.chatId;
    }
    public String getChatName() { return this.chatName; }
}
