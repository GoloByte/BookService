package golo.bookservice.api;

import golo.bookservice.api.dto.Book;
import golo.bookservice.api.dto.Charge;
import golo.bookservice.core.Core;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
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
    public List<Book> lendOut(@RequestParam("quantity") int quantity) {
        return core.lendOut(quantity);
    }
}
