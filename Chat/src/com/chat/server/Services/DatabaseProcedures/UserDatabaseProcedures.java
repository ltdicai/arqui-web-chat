package com.chat.server.Services.DatabaseProcedures;

import com.chat.client.Models.User;
import com.chat.client.errors.UserNotFoundException;
import com.chat.server.Services.ConnectionManager;

import java.sql.*;

public class UserDatabaseProcedures {
    private static Connection connection;

    public UserDatabaseProcedures() throws SQLException {
        /*try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
        }
        String url = "jdbc:postgresql://localhost:5432/gwtdb";
        String username = "postgres";
        String password = "postgres";*/
        connection = ConnectionManager.getConnection();
    }

    public void insert(User user) throws SQLException {

        String insert = "INSERT INTO gwtdbschema.users"
                    + "(userid) VALUES"
                    + "(?)";

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareCall(insert);
        preparedStatement.setString(1, user.getUserID());
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
}
