package com.chat.client.Events;

import com.google.gwt.event.shared.EventHandler;

public interface LogoutEventHandler extends EventHandler {
    void onLogout(LogoutEvent event);
}
