package com.Chat.server.Services;

import com.Chat.client.Models.User;
import com.Chat.server.Services.DatabaseProcedures.UserDatabaseProcedures;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.Chat.client.Services.UserDataService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDataServiceImpl extends RemoteServiceServlet implements UserDataService {

    @Override
    public void insert(User user) {
        try{
            UserDatabaseProcedures userDatabaseProcedures = new UserDatabaseProcedures();
            userDatabaseProcedures.insert(user);
        }
        catch (SQLException ex){
        }
    }

    @Override
    public User get(String userID) {
        try{
            UserDatabaseProcedures userDatabaseProcedures = new UserDatabaseProcedures();
            return userDatabaseProcedures.get(userID);
        }
        catch (SQLException ex){
            System.out.print("Excepcion del user get:" + ex.getMessage());
            return null;
        }
    }
}