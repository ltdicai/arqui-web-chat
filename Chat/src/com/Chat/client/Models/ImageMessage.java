package com.Chat.client.Models;

import java.awt.*;

public class ImageMessage extends Message {
    private Image image;

    public ImageMessage(){}

    public ImageMessage(User user, Image image) {
        super(user);
        this.image = image;
    }

    public Image getImage() {
        return image;
    }
}
