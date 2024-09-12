package com.av.avmessenger.Class;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String phoneNumber;
    private boolean isOnline;

    // Constructor with all fields
    public User(int id, String firstName, String lastName, String imageUrl, String phoneNumber, boolean isOnline) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUrl = imageUrl;
        this.phoneNumber = phoneNumber;
        this.isOnline = isOnline;
    }

    // Constructor with less parameters
    public User(int id, String firstName, String lastName, String imageUrl) {
        this(id, firstName, lastName, imageUrl, "", false); // Default values for missing parameters
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return getFullName(); // Display the full name in the ListView
    }
}
