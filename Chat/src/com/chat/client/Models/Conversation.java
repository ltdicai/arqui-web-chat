package com.chat.client.Models;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Stack;

public abstract class Conversation implements IsSerializable {
    private Stack<Message> messages;

    public Conversation() {
        this.messages = new Stack<>();
    }

    public Stack<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message){
        this.messages.add(message);
    }
}
