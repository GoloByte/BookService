package com.golobyte.bookservice.data;

import com.golobyte.bookservice.data.entity.AuthorEo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<AuthorEo, String> {
    AuthorEo findByName(String name);
}
