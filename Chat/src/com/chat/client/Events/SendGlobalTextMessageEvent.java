package com.chat.client.Events;

import com.google.gwt.event.shared.GwtEvent;

public class SendGlobalTextMessageEvent extends GwtEvent<SendGlobalTextMessageEventHandler> {
    public static Type<SendGlobalTextMessageEventHandler> TYPE = new Type<SendGlobalTextMessageEventHandler>();

    public Type<SendGlobalTextMessageEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(SendGlobalTextMessageEventHandler handler) {
        handler.onSendGlobalTextMessage(this);
    }

    private String messageText;

    public SendGlobalTextMessageEvent(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageText() {
        return messageText;
    }
}
