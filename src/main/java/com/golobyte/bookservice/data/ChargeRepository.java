package com.golobyte.bookservice.data;

import com.golobyte.bookservice.data.entity.ChargeEo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargeRepository extends JpaRepository<ChargeEo, String> {
}
