package golo.bookservice.core;

import golo.bookservice.api.dto.Charge;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

@Slf4j
@Component
@Transactional
public class Importer {
    public Charge importBooks(InputStream inputStream) {
        log.info("import started ...");


        return new Charge();

    }
}