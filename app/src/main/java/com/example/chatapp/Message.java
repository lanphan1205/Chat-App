package com.example.chatapp;

import android.util.Log;

import androidx.annotation.NonNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Message {
    private String sender;
    private String message;
    private Date timeStamp;

    public Message() {

    }

    public Message(String sender, String message, Timestamp timeStamp) {
        this.sender = sender;
        this.message = message;
        this.timeStamp = timeStamp;
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

    public Date getTimeStamp() { return this.timeStamp; }

    public String getFormattedTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM HH:mm");
//        String format = sdf.format(new Date(this.timeStamp.getTime() + 8 * 3600 * 1000));
        String format = sdf.format(this.timeStamp);
        return format;
    }

    @NonNull
    @Override
    public String toString() {
        return "{" + "sender: " + sender + ", " + "msg: " + message + "}";
    }
}
