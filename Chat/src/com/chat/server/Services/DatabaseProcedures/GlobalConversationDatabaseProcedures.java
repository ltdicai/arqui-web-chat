package com.chat.server.Services.DatabaseProcedures;

import com.chat.client.Models.*;
import com.chat.client.errors.UserNotFoundException;
import com.chat.server.Services.ConnectionManager;

import java.sql.*;
import java.util.Iterator;
import java.util.List;

public class GlobalConversationDatabaseProcedures {
    private static Connection connection;

    public GlobalConversationDatabaseProcedures() throws SQLException {
        connection = ConnectionManager.getConnection();
    }

    public GlobalConversation insert(String conversationName) throws SQLException {
        // Create entry on main conversations table

        GlobalConversation globalConversation = new GlobalConversation();

        String insert = "INSERT INTO gwtdbschema.conversations " +
                "(conversationid) VALUES " +
                "(?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setString(1, conversationName);
        preparedStatement.execute();
        globalConversation.setId(conversationName);


        // Create specific entry on GroupConversations table
        insert = "INSERT INTO gwtdbschema.globalconversations"
                + "(conversationid) VALUES"
                + "(?)";

        preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setString(1, globalConversation.getId());
        preparedStatement.execute();

        return globalConversation;

    }

    public GlobalConversation get(int lastmessagenumber) throws SQLException, UserNotFoundException {

        GlobalConversation globalConversation = new GlobalConversation();
        String query = "SELECT *"
                + "FROM gwtdbschema.globalconversations "
                + "LIMIT 1";

        PreparedStatement preparedStatement = connection.prepareCall(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            globalConversation.setId(resultSet.getString("conversationid"));
        }else{
            globalConversation = insert("global");
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
            textMessageDatabaseProcedures.insert((TextMessage) message, "global");
        }
        else if(message.getClass() == ImageMessage.class){
            ImageMessageDatabaseProcedures imageMessageDatabaseProcedures = new ImageMessageDatabaseProcedures();
            imageMessageDatabaseProcedures.insert((ImageMessage) message, "global");
        }
        else if(message.getClass() == AudioMessage.class){
            AudioMessageDatabaseProcedures audioMessageDatabaseProcedures = new AudioMessageDatabaseProcedures();
            audioMessageDatabaseProcedures.insert((AudioMessage) message, "global");
        }
    }

    private void createGlobalConversation(){

    }
}
