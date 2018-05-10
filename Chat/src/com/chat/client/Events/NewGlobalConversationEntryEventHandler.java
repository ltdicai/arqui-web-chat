package com.chat.client.Events;

import com.google.gwt.event.shared.EventHandler;

public interface NewGlobalConversationEntryEventHandler extends EventHandler {
    void onNewGlobalConversationEntry(NewGlobalConversationEntryEvent event);
}
