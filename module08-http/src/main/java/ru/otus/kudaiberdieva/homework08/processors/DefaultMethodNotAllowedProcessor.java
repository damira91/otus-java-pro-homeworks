package ru.otus.kudaiberdieva.homework08.processors;

import ru.otus.kudaiberdieva.homework08.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DefaultMethodNotAllowedProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        String response = "HTTP/1.1 405 Method Not Allowed\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n<html><body><h1>Try to use other method</h1></body></html>";
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}

