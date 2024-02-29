package com.golobyte.bookservice.api;

import com.golobyte.bookservice.core.BookServiceGitProperties;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@Tag(name = "Version", description = "the Version Api")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/version", produces = MediaType.APPLICATION_JSON_VALUE)
public class VersionController {

    private final BookServiceGitProperties bookServiceGitProperties;
    @GetMapping("/version")
    public Map<String, String> getBookServiceGitProperties() {
        return bookServiceGitProperties.getBuildVersionAndCommitId();
    }
}