package ru.otus.kudaiberdieva.homework11.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductDTO {
    private Long id;
    private String title;
    private Double price;
}

