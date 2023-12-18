package com.golobyte.bookservice.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "charge")
public class ChargeEo {
    @Id
    @UuidGenerator
    private String id;

    @Column(name = "imported_on")
    private Instant importedOn;

    @Column(name = "imported_count")
    private long importedCount;

    /*
        One-To-Many relation of the charge to books

        Use Cascading: to leverage JPA's cascading feature to save related entities in a single operation.
        In this case, cascade the save operation from ChargeEo to BookEo.
        This means when  save a ChargeEo entity, all the associated BookEo entities will be saved automatically.

        Use Lazy loading: With FetchType.LAZY, the related entities ( BookEo entities associated with a ChargeEo)
        are not loaded immediately when the parent entity (ChargeEo) is retrieved from the database.
        Instead, they are loaded on-demand, i.e., only when you actually access the collection of related entities in code.
     */
    @OneToMany(mappedBy = "chargeEo", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<BookEo> books;
}
