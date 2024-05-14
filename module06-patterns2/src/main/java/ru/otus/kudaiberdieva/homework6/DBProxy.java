package ru.otus.kudaiberdieva.homework6;

import java.sql.SQLException;
import java.util.List;

public class DBProxy implements DBService {
    private final DBService dbService;

    public DBProxy(String connectionString) {
        dbService = new DBServiceImpl(connectionString);
    }

    @Override
    public void connect() throws SQLException {
        dbService.connect();
    }

    @Override
    public void disconnect() throws SQLException {
        dbService.disconnect();
    }

    @Override
    public void execute(List<String> commands) throws SQLException {
        dbService.execute(commands);
    }

    @Override
    public void initTransaction() throws SQLException {
        dbService.initTransaction();
    }

    @Override
    public void commitTransaction() throws SQLException {
        dbService.commitTransaction();
    }

    @Override
    public void rollbackTransaction() throws SQLException {
        dbService.rollbackTransaction();
    }
}
