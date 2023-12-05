package golo.bookservice.api.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private String name;

    private boolean lendOut;

}
