package ru.otus.kudaiberdieva.homework16.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatSessionsDTO {
    @JsonProperty("chat_sessions")
    private List<ChatSessionDTO> chatSessions;
}
