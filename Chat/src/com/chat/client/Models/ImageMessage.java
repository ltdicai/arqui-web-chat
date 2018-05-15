package com.chat.client.Models;

public class ImageMessage extends Message {
    private String image;

    public ImageMessage(){}

    public ImageMessage(User user, String image) {
        super(user);
        this.image = image;
    }

    public String getImage() {
        return image;
    }
}

