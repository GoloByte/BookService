package com.golobyte.bookservice.api.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Charge {
    private LocalDateTime importedOn;

    private long importedCount;

    private List<Book> books;
}
