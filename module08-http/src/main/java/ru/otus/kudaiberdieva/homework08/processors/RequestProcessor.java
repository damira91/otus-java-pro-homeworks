package ru.otus.kudaiberdieva.homework08.processors;

import ru.otus.kudaiberdieva.homework08.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;


public interface RequestProcessor {
    void execute(HttpRequest httpRequest, OutputStream output) throws IOException;
    default String headerType() {
        return "/";
    }
}

