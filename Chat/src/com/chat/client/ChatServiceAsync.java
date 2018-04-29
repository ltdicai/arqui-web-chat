package com.chat.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ChatServiceAsync {
    void getMessage(String msg, AsyncCallback<String> async);
}
