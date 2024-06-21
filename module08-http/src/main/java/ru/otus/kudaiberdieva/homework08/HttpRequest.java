package ru.otus.kudaiberdieva.homework08;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.kudaiberdieva.homework08.application.SessionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class HttpRequest {
    private String rawRequest;
    private String uri;
    private HttpMethod method;
    private Map<String, String> parameters;
    private Map<String, String> headers;
    private String body;
    private static final String CRLF = "\r\n";
    private String sessionId;
    private static final String SESSIONID = "SESSIONID";

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    public String getRouteKey() {
        return String.format("%s %s", method, uri);
    }

    public String getUri() {
        return uri;
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public String getBody() {
        return body;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public HttpRequest(String rawRequest) {
        this.headers = new HashMap<>();
        this.rawRequest = rawRequest;
        this.parseRequestLine();
        this.tryToParseBody();
        this.sessionId = new SessionManager(getHeader("Cookie")).getSessionId();
        logger.info("sessionIdUUID is {}", this.sessionId);

        logger.debug("\nall-rawRequest\n{}", rawRequest);
        logger.trace("{} {}\nParameters: {}\nBody: {}", method, uri, parameters, body); // TODO правильно все поназывать
    }

    public void tryToParseBody() {
        if (method == HttpMethod.POST || method == HttpMethod.PUT) {
            List<String> lines = rawRequest.lines().collect(Collectors.toList());
            int splitLine = -1;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).isEmpty()) {
                    splitLine = i;
                    break;
                }
            }
            if (splitLine > -1) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = splitLine + 1; i < lines.size(); i++) {
                    stringBuilder.append(lines.get(i));
                }
                this.body = stringBuilder.toString();
            }
        }
    }

    public void parseRequestLine() {
        int startIndex = rawRequest.indexOf(' ');
        int endIndex = rawRequest.indexOf(' ', startIndex + 1);
        this.uri = rawRequest.substring(startIndex + 1, endIndex);
        this.method = HttpMethod.valueOf(rawRequest.substring(0, startIndex));
        this.parameters = new HashMap<>();
        if (uri.contains("?")) {
            String[] elements = uri.split("[?]");
            this.uri = elements[0];
            String[] keysValues = elements[1].split("&");
            for (String o : keysValues) {
                String[] keyValue = o.split("=");
                if (keyValue.length == 2) {
                    this.parameters.put(keyValue[0], keyValue[1]);
                }
            }
        }
        logger.info("\nRaw Request\n{}", rawRequest);

        int headerEnd = rawRequest.indexOf(CRLF) + 2;
        do {
            int headerStart = headerEnd;
            headerEnd = rawRequest.indexOf(":", headerStart);
            String headerName = rawRequest.substring(headerStart, headerEnd).trim();
            headerStart = headerEnd + 2;
            headerEnd = rawRequest.indexOf(CRLF, headerStart);
            String headerValue = rawRequest.substring(headerStart, headerEnd).trim();
            logger.info("Header: {} : {}", headerName, headerValue);
            headers.put(headerName, headerValue);
            headerEnd += 2;
        } while (headerEnd != rawRequest.indexOf(CRLF, headerEnd));
    }
}

