package com.ungratz.okunurmu.Activities;

public class ChatMessage {
    public String sender, receiver, message, date;


    public ChatMessage() {
        // Required empty constructor for Firebase
    }

    public ChatMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() { return date; };

    public String getReceiver() { return receiver; }
}
