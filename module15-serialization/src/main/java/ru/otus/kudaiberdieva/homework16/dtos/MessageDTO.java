package ru.otus.kudaiberdieva.homework16.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class MessageDTO {
    @JsonProperty("ROWID")
    private Long rowId;

    private String attributedBody;

    @JsonProperty("belong_number")
    private String belongNumber;

    private Timestamp date;

    @JsonProperty("date_read")
    private Timestamp dateRead;

    private UUID guid;

    @JsonProperty("handle_id")
    private Long handleId;

    @JsonProperty("has_dd_results")
    private Integer hasDdResults;

    @JsonProperty("is_deleted")
    private Integer isDeleted;

    @JsonProperty("is_from_me")
    private Integer isFromMe;

    @JsonProperty("send_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
    private LocalDateTime sendDate;

    @JsonProperty("send_status")
    private Integer sendStatus;

    private String service;

    private String text;
}
