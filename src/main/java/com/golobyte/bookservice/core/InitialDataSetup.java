package com.golobyte.bookservice.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("import")
public class InitialDataSetup implements CommandLineRunner {

    private final Core core;

    public InitialDataSetup(Core core) {
        this.core = core;
    }

    @Override
    public void run(String... args) {
        try {
            String path = "/books/5books.csv";
            Resource resource = new ClassPathResource(path);
            log.info("Start Async Books import for file : " + path);
            core.importBooksAsync(resource.getInputStream()).thenAccept(charge -> {
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
    }
}