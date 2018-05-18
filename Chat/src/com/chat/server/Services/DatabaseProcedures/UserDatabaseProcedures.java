package com.chat.server.Services.DatabaseProcedures;

import com.chat.client.Models.User;
import com.chat.client.errors.UserInvalidIDOrPassword;
import com.chat.client.errors.UserInvalidPassword;
import com.chat.client.errors.UserNotFoundException;
import com.chat.server.Services.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDatabaseProcedures {
    private static Connection connection;

    public UserDatabaseProcedures() throws SQLException {
        connection = ConnectionManager.getConnection();
    }

    public void insert(User user, String password) throws SQLException {

        String insert = "INSERT INTO gwtdbschema.users"
                    + "(userid, password) VALUES"
                    + "(?, ?)";

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareCall(insert);
        preparedStatement.setString(1, user.getUserID());
        preparedStatement.setString(2, password);
        preparedStatement.execute();
    }


    public User get(String userID) throws UserNotFoundException, SQLException {
        String get = "select * from gwtdbschema.users where userid = ?";

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareCall(get);
        preparedStatement.setString(1, userID);
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet();
        if(!resultSet.next()){
            throw new UserNotFoundException();
        }

        User users = new User(resultSet.getString("userid"));

        return users;

    }

    public List<User> getAll() throws Exception {
        String query = "SELECT * FROM gwtdbschema.users";
        PreparedStatement preparedStatement = connection.prepareCall(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<User> results = new ArrayList<>();
        while(resultSet.next()) {
            User user = new User(resultSet.getString("userId"));
            results.add(user);
        }
        return results;
    }

    public User login(String userID, String password) throws SQLException, UserInvalidPassword, UserNotFoundException, UserInvalidIDOrPassword
    {
        if(userID.length() < 6 && password.length() < 6){
            throw new UserInvalidIDOrPassword();
        }
        String get = "select * from gwtdbschema.users where userid = ? and password = ?";

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareCall(get);
        preparedStatement.setString(1, userID);
        preparedStatement.setString(2, password);
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet();
        if(!resultSet.next()){
            get(userID);
            throw new UserInvalidPassword();
        }

        User users = new User(resultSet.getString("userid"));

        return users;

    }
}
