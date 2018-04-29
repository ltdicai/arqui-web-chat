package com.chat.server.Models;

public abstract class Message {
    private User user;

    public Message(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

