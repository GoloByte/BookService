package golo.bookservice.data;

import golo.bookservice.data.entity.ChargeEo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargeRepository extends JpaRepository<ChargeEo, String> {
}
