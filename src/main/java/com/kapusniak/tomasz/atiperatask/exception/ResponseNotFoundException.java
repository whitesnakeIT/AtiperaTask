package com.kapusniak.tomasz.atiperatask.exception;

public class ResponseNotFoundException extends RuntimeException{
    public ResponseNotFoundException(String message) {
        super(message);
    }

}
