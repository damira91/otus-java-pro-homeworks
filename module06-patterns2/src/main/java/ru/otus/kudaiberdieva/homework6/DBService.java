package ru.otus.kudaiberdieva.homework6;

import java.sql.SQLException;
import java.util.List;

public interface DBService {
    void connect() throws SQLException;

    void disconnect() throws SQLException;

    void execute(List<String> commands) throws SQLException;

    void initTransaction() throws SQLException;

    void commitTransaction() throws SQLException;

    void rollbackTransaction() throws SQLException;


}
