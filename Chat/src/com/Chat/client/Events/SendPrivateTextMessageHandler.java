package com.Chat.client.Events;

import com.google.gwt.event.shared.EventHandler;

public interface SendPrivateTextMessageHandler extends EventHandler {
    void onSendPrivateTextMessage(SendPrivateTextMessage event);
}
