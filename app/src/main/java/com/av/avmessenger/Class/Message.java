package com.av.avmessenger.Class;

public class Message {
    private int senderId;
    private int recipientId;
    private String message;
    private String timestamp;
    private String jour;

    public Message(int senderId, int recipientId, String message, String timestamp, String jour) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.message = message;
        this.timestamp = timestamp;
        this.jour = jour;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getJour() {
        return jour;
    }
}
