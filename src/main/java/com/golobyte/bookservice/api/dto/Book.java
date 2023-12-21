package com.golobyte.bookservice.api.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private String name;

    private boolean borrowed;

    private List<Author> authors;
}
