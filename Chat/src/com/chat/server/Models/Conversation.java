package com.chat.server.Models;

import java.util.Stack;

public abstract class Conversation {
    private Stack<Message> messages;

    public Conversation() {
    }

    public Stack<Message> getMessages() {
        return messages;
    }

    private void addMessage(Message message){
        this.messages.add(message);
    }
}
