package ru.otus.kudaiberdieva.homework17.dto;

import lombok.*;

@ToString
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private String title;
    private int value;

}
