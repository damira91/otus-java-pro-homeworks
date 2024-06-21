package ru.otus.kudaiberdieva.homework08;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.kudaiberdieva.homework08.application.CacheManager;
import ru.otus.kudaiberdieva.homework08.application.processors.*;
import ru.otus.kudaiberdieva.homework08.processors.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private Map<String, RequestProcessor> router;
    private RequestProcessor unknownOperationRequestProcessor;
    private RequestProcessor optionsRequestProcessor;
    private RequestProcessor staticResourcesProcessor;
    private RequestProcessor notAcceptableProcessor;
    private RequestProcessor methodNotAllowedProcessor;
    private final CacheManager cacheManager;
    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class.getName());

    public Dispatcher() {
        this.router = new HashMap<>();
        this.router.put("GET /calc", new CalculatorRequestProcessor());
        this.router.put("GET /hello", new HelloWorldRequestProcessor());
        this.router.put("GET /items", new GetAllProductsProcessor());
        this.router.put("POST /items", new CreateNewProductProcessor());


        this.unknownOperationRequestProcessor = new DefaultUnknownOperationProcessor();
        this.optionsRequestProcessor = new DefaultOptionsProcessor();
        this.staticResourcesProcessor = new DefaultStaticResourcesProcessor();
        this.notAcceptableProcessor = new DefaultNotAcceptableProcessor();
        this.methodNotAllowedProcessor = new DefaultMethodNotAllowedProcessor();
        this.cacheManager = new CacheManager();


        logger.info("Диспетчер проинициализирован");
    }

    public void execute(HttpRequest httpRequest, OutputStream outputStream) throws IOException {

        logger.info("Received request: {} {}", httpRequest.getMethod(), httpRequest.getUri());

        if (httpRequest.getMethod() == HttpMethod.OPTIONS) {
            optionsRequestProcessor.execute(httpRequest, outputStream);
            return;
        }

        String uri = httpRequest.getUri();
        if (Files.exists(Paths.get("module08-http/static/", uri.substring(1)))) {
            serveStaticResource(httpRequest, outputStream);
            return;
        }

        CacheManager.CachedFileResponse cachedResponse = cacheManager.serveCachedResource(httpRequest);
        if (cachedResponse != null) {
            sendCachedResourceResponse(httpRequest, outputStream, cachedResponse);
            return;
        }

        RequestProcessor processor = router.get(httpRequest.getRouteKey());
        if (processor instanceof RequestProcessorHeaderType) {
            String expectedType = ((RequestProcessorHeaderType) processor).headerType();
            if (!HeaderUtils.isValidAcceptHeader(httpRequest, expectedType)) {
                notAcceptableProcessor.execute(httpRequest, outputStream);
                return;
            }
        }

        if (!router.containsKey(httpRequest.getRouteKey())) {
            long count = router.keySet().stream()
                    .filter(route -> route.endsWith(httpRequest.getUri()))
                    .count();
            if (count > 0) {
                methodNotAllowedProcessor.execute(httpRequest, outputStream);
            } else {
                unknownOperationRequestProcessor.execute(httpRequest, outputStream);
            }
            return;
        }

        router.get(httpRequest.getRouteKey()).execute(httpRequest, outputStream);
    }

    private void serveStaticResource(HttpRequest httpRequest, OutputStream outputStream) throws IOException {
        String uri = httpRequest.getUri();
        Path filePath = Paths.get("module08-http/static/", uri.substring(1));

        if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
            byte[] fileContent = Files.readAllBytes(filePath);
            String contentType = cacheManager.getContentType(uri);
            outputStream.write(("HTTP/1.1 200 OK\r\n" +
                    "Content-Type: " + contentType + "\r\n" +
                    "Content-Length: " + fileContent.length + "\r\n" +
                    cacheManager.serveCachedResource(httpRequest).getETagHeader() + "\r\n" +
                    cacheManager.serveCachedResource(httpRequest).getCacheControlHeader() + "\r\n" +
                    cacheManager.serveCachedResource(httpRequest).getLastModifiedHeader() + "\r\n" +
                    "\r\n").getBytes());
            outputStream.write(fileContent);
        } else {
            unknownOperationRequestProcessor.execute(httpRequest, outputStream);
        }
    }

    private void sendCachedResourceResponse(HttpRequest httpRequest, OutputStream outputStream, CacheManager.CachedFileResponse cachedResponse) throws IOException {
        if (cachedResponse.isNotModified()) {
            outputStream.write("HTTP/1.1 304 Not Modified\r\n\r\n".getBytes());
        } else {
            byte[] fileContent = cachedResponse.getContent();
            String contentType = cacheManager.getContentType(httpRequest.getUri());
            outputStream.write(("HTTP/1.1 200 OK\r\n" +
                    "Content-Type: " + contentType + "\r\n" +
                    "Content-Length: " + fileContent.length + "\r\n" +
                    cachedResponse.getETagHeader() + "\r\n" +
                    cachedResponse.getCacheControlHeader() + "\r\n" +
                    cachedResponse.getLastModifiedHeader() + "\r\n" +
                    "\r\n").getBytes());
            outputStream.write(fileContent);
        }
    }
}
