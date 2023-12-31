package com.golobyte.bookservice.data;

import com.golobyte.bookservice.data.entity.BookEo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEo, String> {
    /**
     * borrow number of books
     * <p>
     * additional statements to ensure that in concurrency calls one book can be lent for exactly one caller
     * <p>
     * FOR UPDATE : lock records on read
     * <p>
     * SKIP LOCKED : concurrent transaction will skip locked records
     *
     * @param number of requested books
     * @return list of books
     */
    @Query(value = "SELECT * FROM book WHERE borrowed = false LIMIT :number FOR UPDATE SKIP LOCKED", nativeQuery = true)
    List<BookEo> borrow(@Param("number") int number);

    @Query(value = "SELECT count(id) FROM book  WHERE borrowed = false", nativeQuery = true)
    long getNumberOfAvailableBooks();

    @Query(value = "SELECT EXISTS(SELECT * FROM book where name = :name)", nativeQuery = true)
    boolean isBookExists(@Param("name") String name);
}
