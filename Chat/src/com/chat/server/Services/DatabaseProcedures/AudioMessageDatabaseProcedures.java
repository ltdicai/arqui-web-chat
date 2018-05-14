package com.chat.server.Services.DatabaseProcedures;

import com.chat.client.Models.AudioMessage;
import com.chat.client.Models.TextMessage;
import com.chat.client.Models.User;
import com.chat.client.errors.UserNotFoundException;
import com.chat.server.Services.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Stack;

public class AudioMessageDatabaseProcedures {
    private static Connection connection;

    public AudioMessageDatabaseProcedures() throws SQLException {
        connection = ConnectionManager.getConnection();
    }

    public void insert(AudioMessage message, int conversationid) throws SQLException {

            String insert = "INSERT INTO gwtdbschema.audiomessages"
                    + "(messageid, message) VALUES"
                    + "(?, ?)";

            MessageDataBaseProcedures messageDataProcedures = new MessageDataBaseProcedures();
            int messageid = messageDataProcedures.insert(message, conversationid);

            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareCall(insert);
            preparedStatement.setInt(1, messageid);
            preparedStatement.setString(2, message.getAudio());
            preparedStatement.execute();

    }

    public Stack<AudioMessage> get(int conversationid) throws SQLException, UserNotFoundException {
        String get = "select tm.*, m.userid, m.conversationid from gwtdbschema.audiomessages tm, gwtdbschema.messages m where m.conversationid = ? and tm.messageid = m.messageid order by messageid";

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareCall(get);
        preparedStatement.setInt(1, 1);
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet();

        Stack<AudioMessage> messagesList = new Stack<AudioMessage>();

        UserDatabaseProcedures userDatabaseProcedures = new UserDatabaseProcedures();
        while (resultSet.next()) {
            User user = userDatabaseProcedures.get(resultSet.getString("userid"));
            AudioMessage audioMessage = new AudioMessage(user, resultSet.getString("message"));
            messagesList.add(audioMessage);
        }

        return messagesList;
    }
}
