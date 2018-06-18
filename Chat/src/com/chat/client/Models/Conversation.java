package com.chat.client.Models;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

public abstract class Conversation implements IsSerializable {
    private List<Message> messages;

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Conversation() {
        this.messages = new ArrayList<>();
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message){
        this.messages.add(message);
    }
}
