package com.av.avmessenger.Class;


public class LoginResponse {
    private boolean success;
    private String message;
    private int role;
    private int user_id;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getUserId() {
        return user_id;
    }
    public int getRole() {
        return role;
    }

}

