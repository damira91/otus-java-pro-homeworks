package ru.otus.kudaiberdieva.homework16.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.kudaiberdieva.homework16.dtos.ChatSessionsDTO;
import ru.otus.kudaiberdieva.homework16.services.ChatSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@AllArgsConstructor
public class ChatSessionController {
    private static final Logger logger = LoggerFactory.getLogger(ChatSessionController.class.getName());
    private final ChatSessionService chatSessionService;
    @PostMapping("/api/v1/chat")
    @ResponseStatus(HttpStatus.CREATED)

    public ResponseEntity<String> createNewChatSessionStructure(@RequestBody ChatSessionsDTO body,
                                                           @RequestHeader(value = "Content-Type", defaultValue = "application/json") String contentType) {
        logger.debug("Received request: {}", body);
        if (!contentType.equals("application/json") && !contentType.equals("application/xml")) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Unsupported media type");
        }
        return chatSessionService.convert(contentType, body);
    }
}
