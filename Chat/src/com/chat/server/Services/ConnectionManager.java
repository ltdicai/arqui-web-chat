package com.chat.server.Services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            String url = "jdbc:postgresql://localhost:5432/gwtdb";
            String username = "postgres";
            String password = "postgres";
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException ex) {
                //no se que hacer
            }
        }
        return connection;

    }
}
