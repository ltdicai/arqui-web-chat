package com.chat.client.Models;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class Message implements IsSerializable {
    private User user;

    public Message(){}

    public Message(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

