package com.chat.server.Services.DatabaseProcedures;

import com.chat.client.Models.Message;
import com.chat.client.Models.PrivateConversation;
import com.chat.client.errors.ConversationNotFoundException;
import com.chat.client.errors.UserNotFoundException;
import com.chat.server.Services.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConversationDatabaseProcedures {
    private Connection connection;
    private UserDatabaseProcedures userDatabaseProcedures;

    public ConversationDatabaseProcedures() throws SQLException {
        connection = ConnectionManager.getConnection();
        userDatabaseProcedures = new UserDatabaseProcedures();
    }

    public PrivateConversation getPrivateConversation(Integer conversationId) throws SQLException, ConversationNotFoundException, UserNotFoundException{
        String query = "SELECT privateconversationid, hostuserid, inviteuserid "
                + "FROM gwtdbschema.privateconversations "
                + "WHERE privateconversationid = ? LIMIT 1";

        PreparedStatement preparedStatement = connection.prepareCall(query);
        preparedStatement.setInt(1, conversationId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(!resultSet.next()){
            throw new ConversationNotFoundException();
        }
        PrivateConversation conversation = new PrivateConversation(
                userDatabaseProcedures.get(resultSet.getString("hostuserid")),
                userDatabaseProcedures.get(resultSet.getString("inviteuserid"))
        );
        conversation.setId(conversationId);
        return conversation;
    }

    public void addMessage(Message message) throws SQLException {
    }


    public void insert(PrivateConversation conversation) throws SQLException {
        String insert = "INSERT INTO gwtdbschema.privateconversations"
                + "(hostuserid, inviteuserid) VALUES"
                + "(?,?)";

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareCall(insert);
        preparedStatement.setString(1, conversation.getUserHost().getUserID());
        preparedStatement.setString(2, conversation.getUserInvite().getUserID());
        preparedStatement.execute();
    }
}
