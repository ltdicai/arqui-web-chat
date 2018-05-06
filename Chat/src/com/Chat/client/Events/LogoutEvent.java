package com.Chat.client.Events;

import com.google.gwt.event.shared.GwtEvent;

public class LogoutEvent extends GwtEvent<LogoutEventHandler> {
    public static Type<LogoutEventHandler> TYPE = new Type<LogoutEventHandler>();

    public Type<LogoutEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(LogoutEventHandler handler) {
        handler.onLogout(this);
    }
}
