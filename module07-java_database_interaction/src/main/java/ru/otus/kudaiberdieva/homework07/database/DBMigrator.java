package ru.otus.kudaiberdieva.homework07.database;

import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;


public class DBMigrator {
    private static final Logger log = LoggerFactory.getLogger(DBMigrator.class);
    private DataSource dataSource;

    public DBMigrator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void migrate(String sqlScriptPath) {
        log.info("Starting database migration from script: {}", sqlScriptPath);
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            String sql = loadSqlScript(sqlScriptPath);
            statement.execute(sql);
            log.info("Database migration completed successfully.");
        } catch (SQLException | IOException e) {
            log.error("Database migration failed.", e);
            throw new RuntimeException(e);
        }
    }

    private String loadSqlScript(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        log.info("SQL script loaded successfully.");
        return sb.toString();
    }
}

