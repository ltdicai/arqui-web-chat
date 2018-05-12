package com.chat.server.Services.DatabaseProcedures;

import com.chat.client.Models.GlobalConversation;
import com.chat.client.Models.Message;
import com.chat.client.Models.TextMessage;
import com.chat.client.Models.User;
import com.chat.client.errors.UserNotFoundException;
import com.chat.server.Services.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Stack;

public class GlobalConversationDatabaseProcedures {
    private static Connection connection;

    public GlobalConversationDatabaseProcedures() throws SQLException {
        connection = ConnectionManager.getConnection();
    }

    public GlobalConversation get() throws SQLException, UserNotFoundException {

        GlobalConversation globalConversation = new GlobalConversation();

        TextMessageDatabaseProcedures textMessageDatabaseProcedures = new TextMessageDatabaseProcedures();
        Stack<TextMessage> textMessages = textMessageDatabaseProcedures.get(1);
        Iterator<TextMessage> iterTextMessages = textMessages.iterator();
        while (iterTextMessages.hasNext()){
           globalConversation.addMessage(iterTextMessages.next());
        }

        return globalConversation;

    }

    public void addMessage(Message message) throws SQLException {
        TextMessageDatabaseProcedures textMessageDatabaseProcedures = new TextMessageDatabaseProcedures();
        textMessageDatabaseProcedures.insert((TextMessage) message, 1);
    }
}
