package ru.otus.kudaiberdieva.homework07.exceptions;

public class RepositoryOperationException extends RuntimeException {
    public RepositoryOperationException(String message) {
        super(message);
    }
}
