package com.chat.client.Events;

import com.chat.client.Presenters.GlobalConversationPresenter;
import com.google.gwt.event.shared.GwtEvent;

public class NewGlobalConversationEntryEvent extends GwtEvent<NewGlobalConversationEntryEventHandler> {
    public static Type<NewGlobalConversationEntryEventHandler> TYPE = new Type<NewGlobalConversationEntryEventHandler>();

    public Type<NewGlobalConversationEntryEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(NewGlobalConversationEntryEventHandler handler) {
        handler.onNewGlobalConversationEntry(this);
    }

    private GlobalConversationPresenter globalConversationPresenter;

    public NewGlobalConversationEntryEvent(GlobalConversationPresenter globalConversationPresenter) {
        this.globalConversationPresenter = globalConversationPresenter;
    }

    public GlobalConversationPresenter getGlobalConversationPresenter() {
        return globalConversationPresenter;
    }
}
