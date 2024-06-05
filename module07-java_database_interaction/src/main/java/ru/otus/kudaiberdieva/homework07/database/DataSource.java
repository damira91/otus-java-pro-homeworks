package ru.otus.kudaiberdieva.homework07.database;

import ru.otus.kudaiberdieva.homework07.exceptions.ApplicationInitializationException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSource {
    private Connection connection;
    private String url;

    public DataSource(String url) {
        this.url = url;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
        return connection;
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ApplicationInitializationException("Error connecting to the database", e);
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}