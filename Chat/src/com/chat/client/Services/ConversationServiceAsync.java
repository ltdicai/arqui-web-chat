package com.chat.client.Services;

import com.chat.client.Models.Conversation;
import com.chat.client.Models.Message;
import com.chat.client.Models.PrivateConversation;
import com.chat.client.Models.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ConversationServiceAsync {
    void getPrivateConversation(String conversationid, int lastmessagenumber, AsyncCallback<PrivateConversation> async);
    void addMessage(Conversation conversation, Message message, AsyncCallback<Void> async);
    void getPrivateConversationBetween(User loggedUser, User inviteUser,int lastnumber, AsyncCallback<PrivateConversation> async);
}
