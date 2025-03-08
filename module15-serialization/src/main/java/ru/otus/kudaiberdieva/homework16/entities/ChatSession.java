package ru.otus.kudaiberdieva.homework16.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


import java.time.LocalDateTime;
import java.util.LinkedList;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatSession {
    @JsonProperty("chat_identifier")
    private String chatIdentifier;
    private String last;
    @JsonProperty("belong_number")
    private String belongNumber;
    private LocalDateTime SendDate;
    private String message;
}
