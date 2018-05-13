package com.chat.server.Services.DatabaseProcedures;

import com.chat.client.Models.*;
import com.chat.client.errors.UserNotFoundException;
import com.chat.server.Services.ConnectionManager;

import java.sql.*;
import java.util.Stack;

public class MessageDataBaseProcedures {
    private static Connection connection;

    public MessageDataBaseProcedures() throws SQLException {
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

    public Stack<Message> get(int conversationid) throws SQLException, UserNotFoundException {
        String getTextMessages = "(select tm.*, m.conversationid, m.userid, 'text' as typemessage from gwtdbschema.messages m, gwtdbschema.textmessages tm where m.messageid = tm.messageid)";
        String getAudioMessages = "(select am.*, m.conversationid, m.userid, 'audio' as typemessage from gwtdbschema.messages m, gwtdbschema.audiomessages am where m.messageid = am.messageid)";;
        String getImageMessages = "(select im.*, m.conversationid, m.userid, 'image' as typemessage from gwtdbschema.messages m, gwtdbschema.imagemessages im where m.messageid = im.messageid)";;
        String get = "select * from (" + getTextMessages + " Union " + getAudioMessages + " Union " + getImageMessages + ") as messageJoin WHERE conversationid = ? ORDER by messageid";

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareCall(get);
        preparedStatement.setInt(1, 1);
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet();

        Stack<Message> messagesList = new Stack<>();

        UserDatabaseProcedures userDatabaseProcedures = new UserDatabaseProcedures();
        while (resultSet.next()) {
            User user = userDatabaseProcedures.get(resultSet.getString("userid"));

            Message message;
            String typemessage = resultSet.getString("typemessage");
            switch (typemessage){
                case "text":
                    message = new TextMessage(user, resultSet.getString("message"));
                    break;
                case "audio":
                    message = new AudioMessage(user, resultSet.getString("message"));
                    break;
                default:
                    message = new ImageMessage(user, resultSet.getString("message"));
                    break;
            }
            messagesList.add(message);
        }

        return messagesList;
    }
}
