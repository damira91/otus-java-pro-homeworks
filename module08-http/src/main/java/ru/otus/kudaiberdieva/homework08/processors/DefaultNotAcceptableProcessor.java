package ru.otus.kudaiberdieva.homework08.processors;

import ru.otus.kudaiberdieva.homework08.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DefaultNotAcceptableProcessor implements RequestProcessor {

    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        String response = "HTTP/1.1 406 OK\r\nContent-Type: text/html\r\n\r\n<html><body><h1>NOT ACCEPTABLE!</h1></body></html>";
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
