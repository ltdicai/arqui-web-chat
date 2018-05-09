package com.Chat.client.Services;

import com.Chat.client.Models.GlobalConversation;
import com.Chat.client.Models.Message;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GlobalConversationDataServiceAsync {
    void get(AsyncCallback<GlobalConversation> callback);
    void addMessage(Message message, AsyncCallback<Void> callback);
}
