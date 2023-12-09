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

    @Column(name = "lend_out")
    private boolean lendOut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_id", nullable = false)
    private ChargeEo chargeEo;
}
