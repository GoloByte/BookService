package com.golobyte.bookservice.api.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Charge {
    private LocalDateTime timestamp;

    private long total;

    private long imported;
}
