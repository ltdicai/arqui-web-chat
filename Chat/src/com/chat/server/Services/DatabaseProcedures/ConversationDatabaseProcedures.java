package com.chat.server.Services.DatabaseProcedures;

import com.chat.client.Models.*;
import com.chat.client.errors.ConversationNotFoundException;
import com.chat.client.errors.UserNotFoundException;
import com.chat.server.Services.ConnectionManager;
import com.chat.server.Services.IntegrationService;

import java.sql.*;

public class ConversationDatabaseProcedures {
    private Connection connection;
    private final UserDatabaseProcedures userDatabaseProcedures;
    private final MessageDataBaseProcedures messageDataBaseProcedures;

    public ConversationDatabaseProcedures() throws SQLException {
        connection = ConnectionManager.getConnection();
        userDatabaseProcedures = new UserDatabaseProcedures();
        messageDataBaseProcedures = new MessageDataBaseProcedures();
    }

    public PrivateConversation getPrivateConversation(String conversationId, int lastnumber) throws SQLException, ConversationNotFoundException, UserNotFoundException{
        String query = "SELECT *"
                + "FROM gwtdbschema.privateconversations "
                + "WHERE conversationid = ? LIMIT 1";

        PreparedStatement preparedStatement = connection.prepareCall(query);
        preparedStatement.setString(1, conversationId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(!resultSet.next()){
            throw new ConversationNotFoundException();
        }
        PrivateConversation conversation = new PrivateConversation(
                userDatabaseProcedures.get(resultSet.getString("hostuserid")),
                userDatabaseProcedures.get(resultSet.getString("inviteuserid"))
        );
        conversation.setId(conversationId);

        for (Message message: messageDataBaseProcedures.get(conversationId, lastnumber)) {
            conversation.addMessage(message);
        }
        return conversation;
    }

    public void addMessage(Conversation conversation, Message message) throws SQLException {
        if(message.getClass() == TextMessage.class){
            TextMessageDatabaseProcedures textMessageDatabaseProcedures = new TextMessageDatabaseProcedures();
            textMessageDatabaseProcedures.insert((TextMessage) message, conversation.getId());
            try {
                IntegrationService.sendNewMessage(conversation, (TextMessage) message);
            } catch (Exception exc) {
                // TODO
            }
        }
        else if(message.getClass() == ImageMessage.class){
            ImageMessageDatabaseProcedures imageMessageDatabaseProcedures = new ImageMessageDatabaseProcedures();
            imageMessageDatabaseProcedures.insert((ImageMessage) message, conversation.getId());
        }
        else if(message.getClass() == AudioMessage.class){
            AudioMessageDatabaseProcedures audioMessageDatabaseProcedures = new AudioMessageDatabaseProcedures();
            audioMessageDatabaseProcedures.insert((AudioMessage) message, conversation.getId());
        }

    }


    public PrivateConversation insert(User host, User invite, String customRoomId) throws SQLException {
        // Create entry on main conversations table
        String conversationName;
        if (customRoomId != null) {
            conversationName = customRoomId;
        } else{
            conversationName = host.getUserID() + "&" + invite.getUserID();
        }

        PrivateConversation conversation = new PrivateConversation(host, invite);
        String insert = "INSERT INTO gwtdbschema.conversations " +
                "(conversationid) VALUES " +
                "(?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setString(1, conversationName);
        preparedStatement.execute();

        conversation.setId(conversationName);

        // Create specific entry on privateconversations table
        insert = "INSERT INTO gwtdbschema.privateconversations"
                + "(conversationid, hostuserid, inviteuserid) VALUES"
                + "(?,?,?)";

        preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setString(1, conversation.getId());
        preparedStatement.setString(2, conversation.getUserHost().getUserID());
        preparedStatement.setString(3, conversation.getUserInvite().getUserID());
        preparedStatement.execute();
        return conversation;
    }

    public PrivateConversation getPrivateConversationBetween(User loggedUser, User inviteUser, int lastnumber)
            throws SQLException, ConversationNotFoundException, UserNotFoundException {
        String query = "SELECT * "
                + "FROM gwtdbschema.privateconversations "
                + "WHERE hostuserid = ? AND inviteuserid = ? "
                + "OR hostuserid = ? AND inviteuserid = ?";
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareCall(query);
        preparedStatement.setString(1, loggedUser.getUserID());
        preparedStatement.setString(2, inviteUser.getUserID());
        preparedStatement.setString(3, inviteUser.getUserID());
        preparedStatement.setString(4, loggedUser.getUserID());
        ResultSet resultSet = preparedStatement.executeQuery();
        if(!resultSet.next()) {
            throw new ConversationNotFoundException();
        }
        PrivateConversation result = new PrivateConversation(loggedUser, inviteUser);
        result.setId(resultSet.getString("conversationid"));
        for (Message message: messageDataBaseProcedures.get(result.getId(), lastnumber)) {
            result.addMessage(message);
        }
        return result;
    }
}
