package com.golobyte.bookservice.api.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    private String name;

    private List<Book> books;
}