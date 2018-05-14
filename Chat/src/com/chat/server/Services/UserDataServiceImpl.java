package com.chat.server.Services;

import com.chat.client.Models.User;
import com.chat.client.errors.UserNotFoundException;
import com.chat.server.Services.DatabaseProcedures.UserDatabaseProcedures;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.chat.client.Services.UserDataService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public User get(String userID) throws UserNotFoundException {
        try{
            UserDatabaseProcedures userDatabaseProcedures = new UserDatabaseProcedures();
            return userDatabaseProcedures.get(userID);
        }
        catch (SQLException ex){
            //System.out.println("Excepcion del user get:" + ex.getMessage());
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> results = new ArrayList<>();
        try {
            UserDatabaseProcedures userDatabaseProcedures = new UserDatabaseProcedures();
            results.addAll(userDatabaseProcedures.getAll());
        } catch (Exception exc) {
            //
        }
        return results;
    }
}