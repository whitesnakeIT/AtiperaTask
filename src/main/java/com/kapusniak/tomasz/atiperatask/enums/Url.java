package com.kapusniak.tomasz.atiperatask.enums;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

public enum Url {
    BASE_URL("https://api.github.com/users/***USERNAME***"),
    REPO_URL("https://api.github.com/users/***USERNAME***/repos"),
    BRANCHES_URL("https://api.github.com/repos/***USERNAME***/***REPOSITORY_NAME***/branches");

    private final String url;

    Url(String url) {
        this.url = url;
    }

    public String getUrl(String username) {
        return url.replace("***USERNAME***", username);
    }

    public String getUrl(String username, String repositoryName) {
        return url.replace("***USERNAME***", username)
                .replace("***REPOSITORY_NAME***", repositoryName);
    }
}
