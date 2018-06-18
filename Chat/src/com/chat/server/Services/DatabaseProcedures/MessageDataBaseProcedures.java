package com.chat.server.Services.DatabaseProcedures;

import com.chat.client.Models.*;
import com.chat.client.errors.UserNotFoundException;
import com.chat.server.Services.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Message> get(int conversationid, int lastmessagenumber) throws SQLException, UserNotFoundException {
        String getTextMessages = "(select tm.*, m.conversationid, m.userid, 'text' as typemessage from gwtdbschema.messages m, gwtdbschema.textmessages tm where m.messageid = tm.messageid)";
        String getAudioMessages = "(select am.*, m.conversationid, m.userid, 'audio' as typemessage from gwtdbschema.messages m, gwtdbschema.audiomessages am where m.messageid = am.messageid)";;
        String getImageMessages = "(select im.*, m.conversationid, m.userid, 'image' as typemessage from gwtdbschema.messages m, gwtdbschema.imagemessages im where m.messageid = im.messageid)";;
        String get = "select * from (select row_number() over (order by messageid nulls last) as rownum, * from (" + getTextMessages + " Union " + getAudioMessages + " Union " + getImageMessages + ") as messageJoin WHERE conversationid = ? group by messageid, message, conversationid, userid, typemessage ORDER by messageid) as messageFromStar where messageFromStar.rownum = ? + 1 ";

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareCall(get);
        preparedStatement.setInt(1, conversationid);
        preparedStatement.setInt(2, lastmessagenumber);
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet();

        List<Message> messagesList = new ArrayList<>();

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
