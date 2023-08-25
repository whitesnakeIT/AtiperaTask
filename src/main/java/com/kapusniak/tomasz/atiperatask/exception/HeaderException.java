package com.kapusniak.tomasz.atiperatask.exception;

import lombok.Getter;

@Getter
public class HeaderException extends RuntimeException {

    private String header;
    public HeaderException(String message, String header) {
        super(message);
    }

}
