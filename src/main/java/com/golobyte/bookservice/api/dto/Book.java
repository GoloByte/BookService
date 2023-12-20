package com.golobyte.bookservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private String name;

    private boolean borrowed;

    private List<Author> authors;
}
