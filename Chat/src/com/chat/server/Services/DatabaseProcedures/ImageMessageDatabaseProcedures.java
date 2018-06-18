package com.chat.server.Services.DatabaseProcedures;

import com.chat.client.Models.ImageMessage;
import com.chat.client.Models.User;
import com.chat.client.errors.UserNotFoundException;
import com.chat.server.Services.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Stack;

public class ImageMessageDatabaseProcedures {
    private static Connection connection;

    public ImageMessageDatabaseProcedures() throws SQLException {
        connection = ConnectionManager.getConnection();
    }

    public void insert(ImageMessage message, int conversationid) throws SQLException {

        String insert = "INSERT INTO gwtdbschema.imagemessages"
                + "(messageid, message) VALUES"
                + "(?, ?)";

        MessageDataBaseProcedures messageDataProcedures = new MessageDataBaseProcedures();
        int messageid = messageDataProcedures.insert(message, conversationid);

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareCall(insert);
        preparedStatement.setInt(1, messageid);
        preparedStatement.setString(2, message.getImage());
        preparedStatement.execute();
    }

    public Stack<ImageMessage> get(int conversationid) throws SQLException, UserNotFoundException {
        String get = "select tm.*, m.userid, m.conversationid from gwtdbschema.imagemessages tm, gwtdbschema.messages m where m.conversationid = ? and tm.messageid = m.messageid order by messageid";

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareCall(get);
        preparedStatement.setInt(1, 1);
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet();

        Stack<ImageMessage> messagesList = new Stack<ImageMessage>();

        UserDatabaseProcedures userDatabaseProcedures = new UserDatabaseProcedures();
        while (resultSet.next()) {
            User user = userDatabaseProcedures.get(resultSet.getString("userid"));
            ImageMessage imageMessage = new ImageMessage(user, resultSet.getString("message"));
            messagesList.add(imageMessage);
        }

        return messagesList;
    }
}
