package com.kapusniak.tomasz.atiperatask.exception;

public enum ExceptionMessages {

    USERNAME_NOT_FOUND("Username not found."),
    USERNAME_EMPTY("Username is empty."),
    USERNAME_NULL("Username is null."),
    MAP_NULL("Map is null."),
    MAP_KEY("Map not contains key: "),
    URL_NULL("Url is null."),
    REPOSITORY_NULL("Repository is null."),
    RESPONSE_NULL("Response is null"),
    JSON_PARSE("Error when parsing string: ");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
