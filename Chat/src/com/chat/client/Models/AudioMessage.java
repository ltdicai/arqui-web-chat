package com.chat.client.Models;

public class AudioMessage extends Message {
    private String audio;

    public AudioMessage(){}

    public AudioMessage(User user, String audio) {
        super(user);
        this.audio = audio;
    }

    public String getAudio() {
        return audio;
    }
}
