package golo.bookservice.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;

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

    @Column(name = "timestamp")
    private Instant timestamp;

    @Column(name = "total")
    private long total;

    @Column(name = "imported")
    private long imported;
}
