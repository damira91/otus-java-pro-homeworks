package ru.otus.kudaiberdieva.homework08.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.kudaiberdieva.homework08.HttpRequest;


public class HeaderUtils {
    private static final Logger logger = LoggerFactory.getLogger(HeaderUtils.class);

    public static boolean isValidAcceptHeader(HttpRequest request, String expectedType) {
        String acceptHeader = request.getHeader("Accept");
        logger.info("Accept Header: {}", acceptHeader);
        logger.info("Expected Type: {}", expectedType);
        if (acceptHeader == null) {
            return false;
        }
        return acceptHeader.contains("*/*") || acceptHeader.contains(expectedType);
    }
}
