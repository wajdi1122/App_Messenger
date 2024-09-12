package com.av.avmessenger.Class;

public class Group {
    private int id;
    private String imageUrl;
    private String name;

    public Group(int id, String imageUrl, String name) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }
}
