package com.kapusniak.tomasz.atiperatask.enums;


public enum GitHubUrl {
    BASE_URL("https://api.github.com/users/***USERNAME***"),
    REPO_URL("https://api.github.com/users/***USERNAME***/repos"),
    BRANCH_URL("https://api.github.com/repos/***USERNAME***/***REPOSITORY_NAME***/branches");

    private final String url;

    GitHubUrl(String url) {
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
