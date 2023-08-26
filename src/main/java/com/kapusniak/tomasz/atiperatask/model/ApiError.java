package com.kapusniak.tomasz.atiperatask.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiError {

    private int status;

    private String message;
}
