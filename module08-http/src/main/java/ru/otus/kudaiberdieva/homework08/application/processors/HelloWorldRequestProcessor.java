package ru.otus.kudaiberdieva.homework08.application.processors;

import ru.otus.kudaiberdieva.homework08.HttpRequest;
import ru.otus.kudaiberdieva.homework08.processors.RequestProcessor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class HelloWorldRequestProcessor implements RequestProcessor {

    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        String sessionId = httpRequest.getSessionId();
        String cookie = "";
        if (sessionId == null || sessionId.isEmpty()) {
            sessionId = UUID.randomUUID().toString();
            cookie = "Set-Cookie: SESSIONID=" + sessionId + "\r\n";
        }

        String response = "HTTP/1.1 200 OK\r\n" +
                cookie +
                "Content-Type: text/html\r\n" +
                "Cache-Control: max-age=10\r\n" +
                "\r\n" +
                "<html><body><h1>Hello World!!!</h1></body></html>";

        output.write(response.getBytes(StandardCharsets.UTF_8));
        output.flush();
    }
    @Override
    public String headerType() {
        return "text/html";
    }
}
