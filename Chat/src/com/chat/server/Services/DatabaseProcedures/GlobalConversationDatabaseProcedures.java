package com.chat.server.Services.DatabaseProcedures;

import com.chat.client.Models.GlobalConversation;
import com.chat.client.Models.Message;
import com.chat.client.Models.TextMessage;
import com.chat.client.Models.User;
import com.chat.client.errors.UserNotFoundException;
import com.chat.server.Services.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GlobalConversationDatabaseProcedures {
    private static Connection connection;

    public GlobalConversationDatabaseProcedures() throws SQLException {
        connection = ConnectionManager.getConnection();
    }

    public GlobalConversation get() throws SQLException, UserNotFoundException {
        String get = "select * from textmessage where conversationid = 1 order by messageid";

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareCall(get);
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet();

        GlobalConversation globalConversation = new GlobalConversation();
        UserDatabaseProcedures userDatabaseProcedures = new UserDatabaseProcedures();
        while (resultSet.next()) {
            User user = userDatabaseProcedures.get(resultSet.getString("userid"));
            TextMessage textMessage = new TextMessage(user, resultSet.getString("message"));
            globalConversation.addMessage(textMessage);
        }

        return globalConversation;

    }

    public void addMessage(Message message) throws SQLException {
        TextMessageDatabaseProcedures textMessageDatabaseProcedures = new TextMessageDatabaseProcedures();
        textMessageDatabaseProcedures.insert((TextMessage) message, 1);
    }
}
