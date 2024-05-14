package ru.otus.kudaiberdieva.homework6;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DBServiceImpl implements DBService {
    private static final Logger logger = LoggerFactory.getLogger(DBServiceImpl.class);
    private String connectionString;
    Connection connection;
    Statement statement;


    public DBServiceImpl(String connectionString) {
        this.connectionString = connectionString;
    }

    @Override
    public void connect() throws SQLException {
        connection = DriverManager.getConnection(connectionString);
        logger.info("Connected to database.");
        statement = connection.createStatement();
    }

    @Override
    public void disconnect() throws SQLException {
        connection.close();
        logger.info("Disconnected from database.");
    }

    @Override
    public void execute(List<String> commands) throws SQLException {
        logger.info("Executing: " + commands.toString());
        for (String s : commands) {
            statement.execute(s);
        }
    }

    @Override
    public void initTransaction() throws SQLException {
        logger.info("Init transaction");
        connection.setAutoCommit(false);
    }

    @Override
    public void commitTransaction() throws SQLException {
        logger.info("Commit transaction");
        connection.commit();
        statement = connection.createStatement();

    }

    @Override
    public void rollbackTransaction() throws SQLException {
        System.out.println("Rollback transaction");
        connection.rollback();
        statement = connection.createStatement();
    }
}
