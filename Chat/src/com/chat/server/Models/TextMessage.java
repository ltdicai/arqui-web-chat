package com.chat.server.Models;

public class TextMessage extends Message{
    private String message;

    public TextMessage(User user, String message) {
        super(user);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
