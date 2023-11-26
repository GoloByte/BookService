package golo.bookservice.data;

import golo.bookservice.data.entity.BookEo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEo, String> {
    /**
     * lend out amount of books
     * <p>
     * additional statements to ensure that in concurrency calls one book can be lent for exactly one caller
     * <p>
     * FOR UPDATE : lock records on read
     * <p>
     * SKIP LOCKED : concurrent transaction will skip locked records
     *
     * @param quantity of requested books
     * @return list of books
     */
    @Query(value = "SELECT * FROM book WHERE lend_out = false LIMIT :quantity FOR UPDATE SKIP LOCKED", nativeQuery = true)
    List<BookEo> lendOut(@Param("quantity") int quantity);

    @Query(value = "SELECT count(id) FROM book  WHERE lend_out = false", nativeQuery = true)
    long getAvailableQuantity();

    @Query(value = "SELECT EXISTS(SELECT * FROM book where name = :name)", nativeQuery = true)
    boolean isBookExists(@Param("name") String name);
}
