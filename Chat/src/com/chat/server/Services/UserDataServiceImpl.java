package com.chat.server.Services;

import com.chat.client.Models.User;
import com.chat.client.errors.UserInvalidIDOrPassword;
import com.chat.client.errors.UserInvalidPassword;
import com.chat.client.errors.UserNotFoundException;
import com.chat.server.Services.DatabaseProcedures.UserDatabaseProcedures;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.chat.client.Services.UserDataService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDataServiceImpl extends RemoteServiceServlet implements UserDataService {

    @Override
    public void insert(User user, String password) {
        try{
            UserDatabaseProcedures userDatabaseProcedures = new UserDatabaseProcedures();
            userDatabaseProcedures.insert(user, encrypt(password));
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

    @Override
    public User login(String userID, String password) throws UserInvalidPassword, UserNotFoundException, UserInvalidIDOrPassword{
        try{
            UserDatabaseProcedures userDatabaseProcedures = new UserDatabaseProcedures();
            return userDatabaseProcedures.login(userID, encrypt(password));
        }
        catch (SQLException ex){
            //System.out.println("Excepcion del user get:" + ex.getMessage());
            return null;
        }
    }

    private String encrypt(String password) {
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] passBytes = password.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<digested.length;i++){
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
}