package com.Chat.server.Services.DatabaseProcedures;

import com.Chat.client.Models.TextMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TextMessageDatabaseProcedures {
    private static Connection connection;

    void insert(TextMessage message, int conversationid) throws SQLException {

            String insert = "INSERT INTO textmessages"
                    + "(messageid, message) VALUES"
                    + "(?, ?)";

            MessageDataProcedures messageDataProcedures = new MessageDataProcedures();
            messageDataProcedures.insert(message, conversationid);

            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareCall(insert);
            preparedStatement.setString(1, message.getUser().getUserID());
            preparedStatement.setString(2, message.getMessage());
            preparedStatement.execute();

    }
}
