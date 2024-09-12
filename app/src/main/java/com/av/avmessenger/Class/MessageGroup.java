package com.av.avmessenger.Class;

public class MessageGroup {
    private int id_sender;
    private String message;
    private String timestamp;
    private String jour;

    // Constructor
    public MessageGroup(int id_sender, String message, String timestamp, String jour) {
        this.id_sender = id_sender;
        this.message = message;
        this.timestamp = timestamp;
        this.jour = jour;
    }

    // Getters
    public int getIdSender() {
        return id_sender;
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