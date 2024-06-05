package ru.otus.kudaiberdieva.homework07.exceptions;

public class RepositoryException extends RuntimeException{
    public RepositoryException(String message) {
        super(message);
    }
    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
