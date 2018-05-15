package com.chat.server.Services.DatabaseProcedures;

import com.chat.client.Models.Message;
import com.chat.client.Models.PrivateConversation;
import com.chat.client.Models.User;
import com.chat.client.errors.ConversationNotFoundException;
import com.chat.client.errors.UserNotFoundException;
import com.chat.server.Services.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        // Create entry on main conversations table
        String insert = "INSERT INTO gwtdbschema.conversations DEFAULT VALUES ";
        PreparedStatement preparedStatement = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
            conversation.setId(resultSet.getInt(1));
        }

        // Create specific entry on privateconversations table
        insert = "INSERT INTO gwtdbschema.privateconversations"
                + "(conversationid, hostuserid, inviteuserid) VALUES"
                + "(?,?,?)";

        preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setInt(1, conversation.getId());
        preparedStatement.setString(2, conversation.getUserHost().getUserID());
        preparedStatement.setString(3, conversation.getUserInvite().getUserID());
        preparedStatement.execute();
    }

    public PrivateConversation getPrivateConversationBetween(User loggedUser, User inviteUser)
            throws SQLException, ConversationNotFoundException {
        String query = "SELECT conversationid, hostuserid, inviteuserid "
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
        result.setId(resultSet.getInt("conversationid"));
        return result;
    }
}
