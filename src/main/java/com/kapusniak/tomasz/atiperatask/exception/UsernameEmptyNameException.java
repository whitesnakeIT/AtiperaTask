package com.kapusniak.tomasz.atiperatask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UsernameEmptyNameException extends RuntimeException{
    public UsernameEmptyNameException(String message) {
        super(message);
    }

}
