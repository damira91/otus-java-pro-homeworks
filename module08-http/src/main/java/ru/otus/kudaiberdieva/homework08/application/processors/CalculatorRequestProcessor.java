package ru.otus.kudaiberdieva.homework08.application.processors;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.kudaiberdieva.homework08.HttpRequest;
import ru.otus.kudaiberdieva.homework08.processors.RequestProcessor;
import ru.otus.kudaiberdieva.homework08.processors.RequestProcessorHeaderType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CalculatorRequestProcessor implements RequestProcessor, RequestProcessorHeaderType {

    private static final Logger logger = LoggerFactory.getLogger(CalculatorRequestProcessor.class);

    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        String cookie = "\r\nSet-Cookie: SESSIONID=" + httpRequest.getSessionId();
        logger.info("cookie {}", cookie);
        int a = Integer.parseInt(httpRequest.getParameter("a"));
        int b = Integer.parseInt(httpRequest.getParameter("b"));
        int result = a + b;
        String outMessage = a + " + " + b + " = " + result;
        String response = "HTTP/1.1 200 OK" +
                cookie +
                "\r\nContent-Type: text/html\r\n\r\n<html><body><h1>" +
                outMessage + "</h1></body></html>";
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String headerType() {
        return "text/html";
    }
}
