package ru.otus.kudaiberdieva.homework08.application.processors;

import com.google.gson.Gson;
import ru.otus.kudaiberdieva.homework08.HttpRequest;
import ru.otus.kudaiberdieva.homework08.application.Item;
import ru.otus.kudaiberdieva.homework08.application.Storage;
import ru.otus.kudaiberdieva.homework08.processors.RequestProcessor;
import ru.otus.kudaiberdieva.homework08.processors.RequestProcessorHeaderType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class GetAllProductsProcessor implements RequestProcessor, RequestProcessorHeaderType {

    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        String sessionId = httpRequest.getSessionId();
        List<Item> items = Storage.getItems();
        Gson gson = new Gson();
        String result = "HTTP/1.1 200 OK\r\n" + sessionId +
                "Content-Type: application/json\r\n" +
                "Connection: keep-alive\r\n" +
                "Access-Control-Allow-Origin: *\r\n\r\n" + gson.toJson(items);
        output.write(result.getBytes(StandardCharsets.UTF_8));
        output.flush();
    }

    @Override
    public String headerType() {
        return "application/json";
    }
}
