package com.example.chatapp2;

import androidx.annotation.NonNull;

public class Message {
    private String sender;
    private String message;

    public Message(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return this.sender;
    }

    public String getMessage() {
        return this.message;
    }

    @NonNull
    @Override
    public String toString() {
        return "{" + "sender: " + sender + ", " + "msg: " + message + "}";
    }
}
