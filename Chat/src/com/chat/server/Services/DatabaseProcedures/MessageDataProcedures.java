package com.chat.server.Services.DatabaseProcedures;

import com.chat.client.Models.Message;
import com.chat.server.Services.ConnectionManager;

import java.sql.*;

public class MessageDataProcedures {
    private static Connection connection;

    public MessageDataProcedures() throws SQLException {
        connection = ConnectionManager.getConnection();
    }


    int insert(Message message, int conversationid) throws SQLException {

        String insert = "INSERT INTO gwtdbschema.messages"
                + "(userid, conversationid) VALUES"
                + "(?, ?);";

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, message.getUser().getUserID());
        preparedStatement.setInt(2,  conversationid);
        preparedStatement.executeUpdate();
        ResultSet rs = preparedStatement.getGeneratedKeys();
        if(rs.next()){
            return rs.getInt("messageid");
        }
        return preparedStatement.getGeneratedKeys().getInt("messageid");
    }
}
