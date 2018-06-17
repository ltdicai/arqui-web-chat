package com.chat.server.Services;

import com.chat.client.Models.Conversation;
import com.chat.client.Models.GroupConversation;
import com.chat.client.Models.Message;
import com.chat.client.Models.User;
import com.chat.client.Services.GroupConversationService;
import com.chat.server.Services.DatabaseProcedures.GroupConversationDatabaseProcedures;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.sql.SQLException;
import java.util.List;

public class GroupConversationServiceImpl extends RemoteServiceServlet implements GroupConversationService {
    private GroupConversationDatabaseProcedures db;

    public GroupConversationServiceImpl() {
        initDB();
    }

    private void initDB() {
        try {
            db = new GroupConversationDatabaseProcedures();
        } catch (SQLException exc) {
            System.out.println("Can't create stuff");
        }
    }
    @Override
    public GroupConversation createGroupConversation(User hostUser, String name) {
        try {
            GroupConversation conversation = new GroupConversation(name);
            conversation.addUser(hostUser);
            db.insert(conversation);
            return conversation;
        } catch (Exception exc) {
            // TODO: Handle exceptions
        }
        return null;
    }

    @Override
    public GroupConversation getGroupConversation(int conversationId, int lastmessagenumber) throws Exception {
        return db.getGroupConversation(conversationId, lastmessagenumber);
    }

    @Override
    public void addMessage(Conversation conversation, Message message) {
        try{
            db.addMessage(conversation, message);
        }
        catch (SQLException ex){
            // TODO: Handle exceptions
        }
    }

    @Override
    public List<GroupConversation> getGroupConversationsForUser(User user, int lastnumber) {
        try {
            return db.getGroupConversationsForUser(user, lastnumber);
        } catch (Exception exc) {
            // TODO: Handle exceptions
            return null;
        }
    }

    @Override
    public void addUser(Conversation conversation, User user) {
        try {
            db.addUser(conversation, user);
        } catch (Exception exc) {
            // TODO: Handle exceptions
        }
    }

    @Override
    public List<User> getUserPosibleAdd(Conversation conversation) {
        try {
            return db.getUserPosibleAdd(conversation);
        } catch (Exception exc) {
            // TODO: Handle exceptions
            return null;
        }
    }

}