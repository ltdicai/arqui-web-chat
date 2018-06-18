package com.chat.client.Services;

import com.chat.client.Models.Conversation;
import com.chat.client.Models.Message;
import com.chat.client.Models.PrivateConversation;
import com.chat.client.Models.User;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ConversationService")
public interface ConversationService extends RemoteService {
    PrivateConversation getPrivateConversation(Integer conversationId, int lastmessagenumber) throws Exception;
    void addMessage(Conversation conversation, Message message);
    PrivateConversation getPrivateConversationBetween(User loggedUser, User inviteUser, int lastnumber);
}
