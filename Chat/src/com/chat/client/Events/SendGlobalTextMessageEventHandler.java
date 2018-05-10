package com.chat.client.Events;

import com.google.gwt.event.shared.EventHandler;

public interface SendGlobalTextMessageEventHandler extends EventHandler {
    void onSendGlobalTextMessage(SendGlobalTextMessageEvent event);
}
