package com.Chat.client.Events;

import com.google.gwt.event.shared.GwtEvent;

public class SendPrivateTextMessage extends GwtEvent<SendPrivateTextMessageHandler> {
    public static Type<SendPrivateTextMessageHandler> TYPE = new Type<SendPrivateTextMessageHandler>();

    public Type<SendPrivateTextMessageHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(SendPrivateTextMessageHandler handler) {
        handler.onSendPrivateTextMessage(this);
    }

    public SendPrivateTextMessage(String message){
        this.message = message;
    }

    public String message;
}
