package com.golobyte.bookservice.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "author")
public class AuthorEo {
    @Id
    @UuidGenerator
    private String id;

    @Column(name = "name", unique = true)
    private String name;

    @ManyToMany(mappedBy = "authors", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookEo> books;
}