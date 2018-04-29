package com.chat.server.Models;

import java.awt.*;

public class ImageMessage extends Message {
    private Image image;

    public ImageMessage(User user, Image image) {
        super(user);
        this.image = image;
    }

    public Image getImage() {
        return image;
    }
}
