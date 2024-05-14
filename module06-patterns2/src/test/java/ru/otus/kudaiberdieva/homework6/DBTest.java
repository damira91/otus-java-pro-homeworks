package ru.otus.kudaiberdieva.homework6;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class DBTest {
    private DBService dbService;

    @BeforeEach
    void setUp() {
        final String connectionUrl = "jdbc:h2:mem:test";
        dbService = new DBProxy(connectionUrl);
    }

    @Test
    void runDBTest() {
        try {
            dbService.connect();

            createStudentsTable();
            insertStudent("John Doe");

            queryStudents();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                dbService.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void createStudentsTable() throws SQLException {
        dbService.initTransaction();
        List<String> createTable = new ArrayList<>();
        createTable.add("Create table students (ID int primary key, name varchar(50))");
        dbService.execute(createTable);
        dbService.commitTransaction();
    }

    private void insertStudent(String name) throws SQLException {
        dbService.initTransaction();
        List<String> insertStudent = new ArrayList<>();
        insertStudent.add("Insert into students (ID, name) values (1, '" + name + "')");
        dbService.execute(insertStudent);
        dbService.commitTransaction();
    }

    private void queryStudents() throws SQLException {
        dbService.initTransaction();
        List<String> queryStudents = new ArrayList<>();
        queryStudents.add("Select * from students");
        dbService.execute(queryStudents);
        dbService.commitTransaction();
    }
}

