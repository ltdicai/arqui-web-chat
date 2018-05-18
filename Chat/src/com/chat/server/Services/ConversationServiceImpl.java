package com.chat.server.Services;

import com.chat.client.Models.Conversation;
import com.chat.client.Models.Message;
import com.chat.client.Models.PrivateConversation;
import com.chat.client.Models.User;
import com.chat.client.Services.ConversationService;
import com.chat.client.errors.ConversationNotFoundException;
import com.chat.server.Services.DatabaseProcedures.ConversationDatabaseProcedures;
import com.chat.server.Services.DatabaseProcedures.UserDatabaseProcedures;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.sql.SQLException;

public class ConversationServiceImpl extends RemoteServiceServlet implements ConversationService {
    private ConversationDatabaseProcedures db;

    public ConversationServiceImpl() {
        initDB();
    }

    private void initDB() {
        try {
            db = new ConversationDatabaseProcedures();
        } catch (SQLException exc) {
            System.out.println("Can't create stuff");
        }
    }

    public PrivateConversation createPrivateConversation(User hostUser, User inviteUser) {
        try {
            PrivateConversation conversation = new PrivateConversation(hostUser, inviteUser);
            db.insert(conversation);
            return conversation;
        } catch (Exception exc) {
            // TODO: Handle exceptions
        }
        return null;
    }

    @Override
    public PrivateConversation getPrivateConversation(Integer conversationId, int lastmessagenumber) throws Exception {
        return db.getPrivateConversation(conversationId, lastmessagenumber);
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
    public PrivateConversation getPrivateConversationBetween(User loggedUser, User inviteUser, int lastnumber) {
        try {
            return db.getPrivateConversationBetween(loggedUser, inviteUser, lastnumber);
        } catch (ConversationNotFoundException exc) {
            return createPrivateConversation(loggedUser, inviteUser);
        } catch (Exception exc) {
            // TODO: Handle exceptions
            return null;
        }
    }

}