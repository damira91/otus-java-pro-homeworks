package ru.otus.kudaiberdieva.homework16.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private String first;
    @JsonProperty("handle_id")
    private Long handleId;
    @JsonProperty("image_path")
    private String imagePath;
    private String last;
    private String middle;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private String service;
    @JsonProperty("thumb_path")
    private String thumbPath;
}
