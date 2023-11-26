package golo.bookservice.api.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private String name;

    private boolean lendOut;

}
