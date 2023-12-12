package com.golobyte.bookservice.api;

import com.golobyte.bookservice.api.dto.Book;
import com.golobyte.bookservice.api.dto.Charge;
import com.golobyte.bookservice.core.Core;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Tag(name = "Books", description = "the Books Api")
@Validated
@RestController
@AllArgsConstructor
@RequestMapping(value = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
public class Controller {

    private final Core core;
    /**
     * import a books source file in database
     *
     * @param file with books
     * @return Charge
     */
    @PostMapping(value = "import", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation( summary = "Import books", description = "import and store books in the database")
    public Charge importBooks(@RequestParam("file") MultipartFile file) {
        return core.importBooks(file);
    }

    /**
     * getCharges provides a list of charges
     *
     * @return list of Charge
     */
    @GetMapping("charges")
    public List<Charge> getCharges() {
        return core.getCharges();
    }

    /**
     * getAmount returns amount of available books
     *
     * @return amount of available books
     */
    @GetMapping("available-quantity")
    public long getAvailableQuantity() {
        return core.getAvailableQuantity();
    }


    /**
     * exclusive lend out of books
     *
     * @param quantity of required books
     * @return List of books
     */
    @GetMapping("lend-out")
    public List<Book> lendOut(@RequestParam("quantity") @Min(1) @Max(5) int quantity) {
        return core.lendOut(quantity);
    }
}