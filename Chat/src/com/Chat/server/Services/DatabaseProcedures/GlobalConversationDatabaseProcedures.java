package com.Chat.server.Services.DatabaseProcedures;

import com.Chat.client.Models.GlobalConversation;
import com.Chat.client.Models.Message;
import com.Chat.client.Models.TextMessage;
import com.Chat.client.Models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GlobalConversationDatabaseProcedures {
    private static Connection connection;

    public GlobalConversation get() throws SQLException {
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
