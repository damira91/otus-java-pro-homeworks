package ru.otus.kudaiberdieva.homework07.exceptions;

import java.sql.SQLException;

public class ApplicationInitializationException extends RuntimeException {
    public ApplicationInitializationException(String message, SQLException e) {
        super(message);
    }

}