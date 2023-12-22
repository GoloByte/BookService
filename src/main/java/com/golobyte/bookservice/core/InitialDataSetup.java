package com.golobyte.bookservice.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@Profile("import")
public class InitialDataSetup implements CommandLineRunner {

    private final Core core;
    private final Importer importer;

    public InitialDataSetup(Core core, Importer importer) {
        this.core = core;
        this.importer = importer;
    }

    @Override
    public void run(String... args) {
        log.info("Commandline runner enter");

        try {
            String path = "/books/5books.csv";
            Resource resource = new ClassPathResource(path);
            InputStream inputStream = resource.getInputStream();
            log.info("Fire in the Main Thread the Async Books Import for file : " + path);
            CompletableFuture.supplyAsync(() -> {
                        log.info("Execute in Worker Tread the importer.importBooks()");
                        return importer.importBooks(inputStream);
                    })
                    .thenAccept(charge -> {
                        // Handle successful import
                        if (!charge.getBooks().isEmpty()) {
                            log.info("Import successful and data is imported: " + charge);
                        } else {
                            log.info("Import completed successfully, but no new data was imported as it already exists in the database: " + charge);
                        }
                    }).exceptionally(ex -> {
                        // Handle exceptions
                        log.error("Import failed: " + ex.getMessage());
                        return null;
                    });
        } catch (Exception e) {
            log.error("Failed to load import file: " + e.getMessage());
        }
        log.info("Commandline runner done");
    }
}