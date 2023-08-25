package com.kapusniak.tomasz.atiperatask.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class ApiError {

    private HttpStatus status;

    private String message;
}
