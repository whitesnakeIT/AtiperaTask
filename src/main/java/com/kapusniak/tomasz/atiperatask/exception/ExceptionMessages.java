package com.kapusniak.tomasz.atiperatask.exception;

public enum ExceptionMessages {

    USERNAME_NOT_FOUND("Username not found."),
    USERNAME_EMPTY("Username is empty."),
    USERNAME_NULL("Username is null."),
    MAP_NOT_FOUND("Map not found."),
    MAP_NULL("Map is null."),

    URL_NOT_FOUND("Url not found."),
    URL_NULL("Url is null."),
    REPOSITORY_NULL("Repository is null."),

    REPOSITORY_NOT_FOUND("Repository not found."),
    RESPONSE_NULL("Response is null"),

    HEADER_NOT_SUPPORTED("Header not supported: "),
    HEADER_NOT_ACCEPTABLE("Header not acceptable: ");
//    XML_NOT_SUPPORTED("XML not supported. Please use application/json.");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
