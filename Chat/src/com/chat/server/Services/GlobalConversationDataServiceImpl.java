package com.chat.server.Services;

import com.chat.client.Models.GlobalConversation;
import com.chat.client.Models.Message;
import com.chat.client.errors.UserNotFoundException;
import com.chat.server.Services.DatabaseProcedures.GlobalConversationDatabaseProcedures;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.chat.client.Services.GlobalConversationDataService;

import java.sql.SQLException;

public class GlobalConversationDataServiceImpl extends RemoteServiceServlet implements GlobalConversationDataService {

    @Override
    public GlobalConversation get()  {
        try{
            GlobalConversationDatabaseProcedures globalConversationDatabaseProcedures = new GlobalConversationDatabaseProcedures();
            return globalConversationDatabaseProcedures.get();
        }
        catch (SQLException ex){
            return null;
        }
        catch (UserNotFoundException ex) {
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