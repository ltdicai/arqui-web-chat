package com.chat.client.Services;

import com.chat.client.Models.Conversation;
import com.chat.client.Models.GroupConversation;
import com.chat.client.Models.Message;
import com.chat.client.Models.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface GroupConversationServiceAsync {
    void getGroupConversation(String conversationid, int lastmessagenumber, AsyncCallback<GroupConversation> async);
    void addMessage(Conversation conversation, Message message, AsyncCallback<Void> async);
    void getGroupConversationsForUser(User user, int lastnumber, AsyncCallback<List<GroupConversation>> async);
    void createGroupConversation(User hostUser, String name, AsyncCallback<Void> async);
    void addUser(Conversation conversation, User user, AsyncCallback<Void> async);
    void getUserPosibleAdd(Conversation conversation, AsyncCallback<List<User>> async);
}