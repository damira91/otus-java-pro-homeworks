package ru.otus.kudaiberdieva.homework16.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.otus.kudaiberdieva.homework16.dtos.ChatSessionDTO;
import ru.otus.kudaiberdieva.homework16.dtos.ChatSessionsDTO;
import ru.otus.kudaiberdieva.homework16.dtos.MemberDTO;
import ru.otus.kudaiberdieva.homework16.dtos.MessageDTO;
import ru.otus.kudaiberdieva.homework16.entities.ChatSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ChatSessionService {
    private static final Logger logger = LoggerFactory.getLogger(ChatSessionService.class.getName());
    private final ObjectMapper jsonMapper;
    private final static String FILE_NAME = "chatSessions.json";
    private XmlMapper xmlMapper;

    public ChatSessionService() {
        jsonMapper = new ObjectMapper();
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonMapper.registerModule(new JavaTimeModule());
        jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    }

    public ResponseEntity<String> convert(String contentType, ChatSessionsDTO chatSessionsDTO) {
        List<ChatSession> chatList = convertToListChatSession(chatSessionsDTO);
        chatList = sortByDate(chatList);

        writeInFile(chatList);

        List<ChatSession> newChatList = readFromFile();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
        try {
            if (contentType.equals(MediaType.APPLICATION_JSON_VALUE)) {
                return new ResponseEntity<>(jsonMapper.writeValueAsString(newChatList), headers, HttpStatus.OK);
            } else if (contentType.equals(MediaType.APPLICATION_XML_VALUE)) {
                xmlMapper = new XmlMapper();
                xmlMapper.registerModule(new JavaTimeModule());
                xmlMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                return new ResponseEntity<>(xmlMapper.writeValueAsString(newChatList), headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unsupported media type", headers, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error reading from file: " + FILE_NAME, e);
        }
    }

    private static List<ChatSession> sortByDate(List<ChatSession> chatSessionList) {
        chatSessionList = chatSessionList.stream()
                .sorted(Comparator.comparing(ChatSession::getSendDate))
                .collect(Collectors.toList());
        return chatSessionList;
    }

    private void writeInFile(List<ChatSession> chatSessionList) {
        try {
            jsonMapper.writeValue(new File(FILE_NAME), chatSessionList);
        } catch (IOException e) {
            logger.debug("Error in writing file", e.getMessage());
        }
    }

    private List<ChatSession> readFromFile() {
        try {
            return jsonMapper.readValue(new File(FILE_NAME),
                    jsonMapper.getTypeFactory().constructCollectionType(List.class, ChatSession.class));
        } catch (IOException e) {
            logger.debug("Error in reading file", e.getMessage());
            return new ArrayList<>();
        }
    }

    private static List<ChatSession> convertToListChatSession(ChatSessionsDTO chatSessionsDto) {
        List<ChatSession> chatSessionList = new ArrayList<>();
        for (ChatSessionDTO chatSession : chatSessionsDto.getChatSessions()) {
            for (MemberDTO member : chatSession.getMembers()) {
                for (MessageDTO message : chatSession.getMessages()) {
                    if (Objects.equals(message.getHandleId(), member.getHandleId())) {
                        ChatSession newChatSession = new ChatSession(chatSession.getChatIdentifier(),
                                member.getLast(),
                                message.getBelongNumber(),
                                message.getSendDate(),
                                message.getText());
                        chatSessionList.add(newChatSession);
                    }
                }
            }
        }
        return chatSessionList;
    }
}
