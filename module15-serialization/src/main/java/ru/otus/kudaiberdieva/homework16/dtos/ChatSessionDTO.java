package ru.otus.kudaiberdieva.homework16.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatSessionDTO {
    private Long chatId;
    @JsonProperty("chat_identifier")
    private String chatIdentifier;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("is_deleted")
    private Integer isDeleted;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MemberDTO> members;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MessageDTO> messages;
}
