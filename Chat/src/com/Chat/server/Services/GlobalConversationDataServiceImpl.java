package com.Chat.server.Services;

import com.Chat.client.Models.GlobalConversation;
import com.Chat.client.Models.Message;
import com.Chat.client.Models.User;
import com.Chat.server.Services.DatabaseProcedures.GlobalConversationDatabaseProcedures;
import com.Chat.server.Services.DatabaseProcedures.UserDatabaseProcedures;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.Chat.client.Services.GlobalConversationDataService;

import java.sql.SQLException;

public class GlobalConversationDataServiceImpl extends RemoteServiceServlet implements GlobalConversationDataService {

    @Override
    public GlobalConversation get() {
        try{
            GlobalConversationDatabaseProcedures globalConversationDatabaseProcedures = new GlobalConversationDatabaseProcedures();
            return globalConversationDatabaseProcedures.get();
        }
        catch (SQLException ex){
            return null;
        }
    }

    @Override
    public void addMessage(Message message) {
        try{
            GlobalConversationDatabaseProcedures globalConversationDatabaseProcedures = new GlobalConversationDatabaseProcedures();
            globalConversationDatabaseProcedures.addMessage(message);
        }
        catch (SQLException ex){
        }
    }

}