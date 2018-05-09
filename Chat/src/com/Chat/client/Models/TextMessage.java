package com.Chat.client.Models;

public class TextMessage extends Message{
    private String message;

    public TextMessage(){

    }

    public TextMessage(User user, String message) {
        super(user);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
