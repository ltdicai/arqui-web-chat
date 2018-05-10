package com.chat.server.Services.DatabaseProcedures;

import com.chat.client.Models.Message;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MessageDataProcedures {
    private static Connection connection;

    void insert(Message message, int conversationid) throws SQLException {

        String insert = "INSERT INTO messages"
                + "(userid, conversationid) VALUES"
                + "(?, ?)";

        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(conversationid);

        String conversationidString =  sb.toString();

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareCall(insert);
        preparedStatement.setString(1, message.getUser().getUserID());
        preparedStatement.setString(2,  conversationidString);
        preparedStatement.execute();

    }
}
