package com.golobyte.bookservice.core;

import com.golobyte.bookservice.api.VersionController;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

@Component
public class BookServiceGitProperties {
    public Map<String, String> getBuildVersionAndCommitId() {
        InputStream resourceAsStream = VersionController.class.getClassLoader()
            .getResourceAsStream("git.properties");
        Properties properties = new Properties();
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        String buildVersion = properties.getProperty("git.build.version");
        String commitId = properties.getProperty("git.commit.id.abbrev");

        return Map.of("version", buildVersion, "commit", commitId);
    }
}
