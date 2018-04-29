package com.chat.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.chat.client.ChatService;

public class ChatServiceImpl extends RemoteServiceServlet implements ChatService {
    // Implementation of sample interface method
    public String getMessage(String msg) {
        return "Client said: \"" + msg + "\"<br>Server answered: \"Hi!\"";
    }
}