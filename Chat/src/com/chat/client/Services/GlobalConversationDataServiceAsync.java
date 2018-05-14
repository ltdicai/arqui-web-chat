package com.chat.client.Services;

import com.chat.client.Models.GlobalConversation;
import com.chat.client.Models.Message;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GlobalConversationDataServiceAsync {
    void get(int lastmessagenumber, AsyncCallback<GlobalConversation> callback);
    void addMessage(Message message, AsyncCallback<Void> callback);
}
