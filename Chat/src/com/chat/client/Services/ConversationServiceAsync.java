package com.chat.client.Services;

import com.chat.client.Models.Conversation;
import com.chat.client.Models.GlobalConversation;
import com.chat.client.Models.Message;
import com.chat.client.Models.PrivateConversation;
import com.chat.client.Models.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

public interface ConversationServiceAsync {
    void getPrivateConversation(Integer conversationId, AsyncCallback<PrivateConversation> async);
    void addMessage(Conversation conversation, Message message, AsyncCallback<Void> async);
    void getPrivateConversationBetween(User loggedUser, User inviteUser, AsyncCallback<PrivateConversation> async);
}
