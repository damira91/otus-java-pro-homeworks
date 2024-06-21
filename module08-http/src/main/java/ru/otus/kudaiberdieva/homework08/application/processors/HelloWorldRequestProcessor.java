package ru.otus.kudaiberdieva.homework08.application.processors;

import ru.otus.kudaiberdieva.homework08.HttpRequest;
import ru.otus.kudaiberdieva.homework08.processors.RequestProcessor;
import ru.otus.kudaiberdieva.homework08.processors.RequestProcessorHeaderType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HelloWorldRequestProcessor implements RequestProcessor, RequestProcessorHeaderType {

    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        String cookie = "\r\nSet-Cookie: SESSIONID=" + httpRequest.getSessionId();
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "Cache-Control: max-age=10\r\n" +
                "\r\n" + cookie +
                "<html><body><h1>Hello World!!!</h1></body></html>";
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String headerType() {
        return "text/html";
    }
}
