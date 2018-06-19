package com.chat.client.Services;

import com.chat.client.Models.Conversation;
import com.chat.client.Models.GroupConversation;
import com.chat.client.Models.Message;
import com.chat.client.Models.User;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("GroupConversationService")
public interface GroupConversationService extends RemoteService {

    GroupConversation getGroupConversation(String conversationid, int lastmessagenumber) throws Exception;
    void addMessage(Conversation conversation, Message message);
    List<GroupConversation> getGroupConversationsForUser(User user, int lastnumber);
    void createGroupConversation(User hostUser, String name);
    void addUser(Conversation conversation, User user);
    List<User> getUserPosibleAdd(Conversation conversation);
    void createGroupConversationWith(User hostUser, String name, List<User> members);

}
