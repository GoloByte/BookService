package com.golobyte.bookservice.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
public class BookEo {
    @Id
    @UuidGenerator
    private String id;

    @Column(name = "name", unique=true)
    private String name;

    @Column(name = "borrowed")
    private boolean borrowed;

    /*
        Many-To-One relation of the books to a charge

        Use Lazy loading:
        The default fetch type for @OneToMany and @ManyToMany associations in JPA is LAZY
        But in contrast,
        for @OneToOne and @ManyToOne associations
        the default fetch type is EAGER.

        So, set the fetch type here explicitly to LAZY
    */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_id", nullable = false)
    private ChargeEo chargeEo;
}
