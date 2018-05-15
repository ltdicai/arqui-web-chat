package com.chat.client.Models;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;
import java.util.Stack;

public abstract class Conversation implements IsSerializable {
    private List<Message> messages;

    public Conversation() {
        this.messages = new Stack<>();
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message){
        this.messages.add(message);
    }
}
