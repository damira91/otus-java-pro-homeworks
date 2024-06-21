package ru.otus.kudaiberdieva.homework08.application;

import ru.otus.kudaiberdieva.homework08.HttpRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheManager {
    private final Map<String, CachedFileResponse> fileCache = new ConcurrentHashMap<>();

    public CachedFileResponse serveCachedResource(HttpRequest httpRequest) throws IOException {
        String uri = httpRequest.getUri();
        Path filePath = Paths.get("module08-http/static/", uri.substring(1));

        if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
            String currentETag = calculateETag(filePath);
            String ifNoneMatch = httpRequest.getHeaders().get("If-None-Match");

            if (currentETag.equals(ifNoneMatch)) {
                return new CachedFileResponse(null, currentETag, true); // Resource not modified
            } else {
                byte[] fileContent = Files.readAllBytes(filePath);
                fileCache.put(uri, new CachedFileResponse(fileContent, currentETag, false));
                return new CachedFileResponse(fileContent, currentETag, false);
            }
        } else {
            return null;
        }
    }

    private String calculateETag(Path filePath) throws IOException {
        String content = new String(Files.readAllBytes(filePath));
        return "W/\"" + content.hashCode() + "\"";
    }

    public String getContentType(String uri) {
        if (uri.endsWith(".html") || uri.endsWith(".htm")) {
            return "text/html";
        } else if (uri.endsWith(".css")) {
            return "text/css";
        } else if (uri.endsWith(".js")) {
            return "text/javascript";
        } else if (uri.endsWith(".png")) {
            return "image/png";
        } else if (uri.endsWith(".jpg") || uri.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (uri.endsWith(".gif")) {
            return "image/gif";
        } else {
            return "application/octet-stream";
        }
    }

    public static class CachedFileResponse {
        private final byte[] content;
        private final String eTag;
        private final boolean notModified;

        public CachedFileResponse(byte[] content, String eTag, boolean notModified) {
            this.content = content;
            this.eTag = eTag;
            this.notModified = notModified;
        }

        public byte[] getContent() {
            return content;
        }

        public String getETag() {
            return eTag;
        }

        public boolean isNotModified() {
            return notModified;
        }

        public String getCacheControlHeader() {
            if (notModified) {
                return "Cache-Control: no-cache";
            } else {
                return "Cache-Control: max-age=3600";
            }
        }

        public String getLastModifiedHeader() {
            if (!notModified) {
                ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
                return "Last-Modified: " + now.format(DateTimeFormatter.RFC_1123_DATE_TIME);
            } else {
                return null;
            }
        }

        public String getETagHeader() {
            if (!notModified) {
                return "ETag: " + eTag;
            } else {
                return null;
            }
        }
    }
}