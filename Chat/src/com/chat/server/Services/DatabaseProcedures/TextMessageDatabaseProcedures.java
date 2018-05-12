package com.chat.server.Services.DatabaseProcedures;

import com.chat.client.Models.GlobalConversation;
import com.chat.client.Models.TextMessage;
import com.chat.client.Models.User;
import com.chat.client.errors.UserNotFoundException;
import com.chat.server.Services.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TextMessageDatabaseProcedures {
    private static Connection connection;

    public TextMessageDatabaseProcedures() throws SQLException {
        connection = ConnectionManager.getConnection();
    }

    void insert(TextMessage message, int conversationid) throws SQLException {

            String insert = "INSERT INTO gwtdbschema.textmessages"
                    + "(messageid, message) VALUES"
                    + "(?, ?)";

            MessageDataProcedures messageDataProcedures = new MessageDataProcedures();
            int messageid = messageDataProcedures.insert(message, conversationid);

            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareCall(insert);
            preparedStatement.setInt(1, messageid);
            preparedStatement.setString(2, message.getMessage());
            preparedStatement.execute();

    }

    public Stack<TextMessage> get(int conversationid) throws SQLException, UserNotFoundException {
        String get = "select tm.*, m.userid, m.conversationid from gwtdbschema.textmessages tm, gwtdbschema.messages m where m.conversationid = ? and tm.messageid = m.messageid order by messageid";

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareCall(get);
        preparedStatement.setInt(1, 1);
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet();

        Stack<TextMessage> messagesList = new Stack<TextMessage>();

        UserDatabaseProcedures userDatabaseProcedures = new UserDatabaseProcedures();
        while (resultSet.next()) {
            User user = userDatabaseProcedures.get(resultSet.getString("userid"));
            TextMessage textMessage = new TextMessage(user, resultSet.getString("message"));
            messagesList.add(textMessage);
        }

        return messagesList;
    }
}
