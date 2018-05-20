package com.chat.server.Services.DatabaseProcedures;

import com.chat.client.Models.*;
import com.chat.client.errors.UserNotFoundException;
import com.chat.server.Services.ConnectionManager;
import com.google.gwt.validation.client.impl.metadata.MessageAndPath;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class GlobalConversationDatabaseProcedures {
    private static Connection connection;

    public GlobalConversationDatabaseProcedures() throws SQLException {
        connection = ConnectionManager.getConnection();
    }

    public GlobalConversation get(int lastmessagenumber) throws SQLException, UserNotFoundException {

        GlobalConversation globalConversation = new GlobalConversation();
        String query = "SELECT *"
                + "FROM gwtdbschema.globalconversations "
                + "LIMIT 1";

        PreparedStatement preparedStatement = connection.prepareCall(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            globalConversation.setId(resultSet.getInt("conversationid"));
        }

        MessageDataBaseProcedures messageDatabaseProcedures = new MessageDataBaseProcedures();
        List<Message> messages = messageDatabaseProcedures.get(globalConversation.getId(), lastmessagenumber);
        Iterator<Message> iterTextMessages = messages.iterator();
        while (iterTextMessages.hasNext()){
           globalConversation.addMessage(iterTextMessages.next());
        }

        return globalConversation;

    }

    public void addMessage(Message message) throws SQLException {
        if(message.getClass() == TextMessage.class){
            TextMessageDatabaseProcedures textMessageDatabaseProcedures = new TextMessageDatabaseProcedures();
            textMessageDatabaseProcedures.insert((TextMessage) message, 1);
        }
        else if(message.getClass() == ImageMessage.class){
            ImageMessageDatabaseProcedures imageMessageDatabaseProcedures = new ImageMessageDatabaseProcedures();
            imageMessageDatabaseProcedures.insert((ImageMessage) message, 1);
        }
        else if(message.getClass() == AudioMessage.class){
            AudioMessageDatabaseProcedures audioMessageDatabaseProcedures = new AudioMessageDatabaseProcedures();
            audioMessageDatabaseProcedures.insert((AudioMessage) message, 1);
        }
    }
}
