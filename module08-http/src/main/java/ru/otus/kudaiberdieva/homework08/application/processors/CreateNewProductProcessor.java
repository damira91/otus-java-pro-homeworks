package ru.otus.kudaiberdieva.homework08.application.processors;

import com.google.gson.Gson;
import ru.otus.kudaiberdieva.homework08.HttpRequest;
import ru.otus.kudaiberdieva.homework08.application.Item;
import ru.otus.kudaiberdieva.homework08.application.Storage;
import ru.otus.kudaiberdieva.homework08.processors.RequestProcessor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;


public class CreateNewProductProcessor implements RequestProcessor {

    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        Gson gson = new Gson();
        Item item = gson.fromJson(httpRequest.getBody(), Item.class);
        Storage.save(item);
        String jsonOutItem = gson.toJson(item);

        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/json\r\n" +
                "Connection: keep-alive\r\n" +
                "Access-Control-Allow-Origin: *\r\n" +
                "\r\n" + jsonOutItem;
        output.write(response.getBytes(StandardCharsets.UTF_8));

    }

    @Override
    public String headerType() {
        return "application/json";
    }
}
