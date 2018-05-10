package com.chat.client.Models;

import com.google.gwt.media.client.Audio;

public class AudioMessage extends Message {
    private Audio audio;

    public AudioMessage(){}

    public AudioMessage(User user, Audio audio) {
        super(user);
        this.audio = audio;
    }

    public Audio getAudio() {
        return audio;
    }
}