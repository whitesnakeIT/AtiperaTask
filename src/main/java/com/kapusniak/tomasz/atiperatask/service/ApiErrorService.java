package com.kapusniak.tomasz.atiperatask.service;

import com.kapusniak.tomasz.atiperatask.exception.ExceptionMessages;
import com.kapusniak.tomasz.atiperatask.exception.JsonParsingException;
import com.kapusniak.tomasz.atiperatask.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class ApiErrorService {

    public ApiError buildApiError(HttpClientErrorException.NotFound ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ExceptionMessages.USERNAME_NOT_FOUND.toString());
        apiError.setStatus(ex.getStatusCode().value());

        return apiError;
    }

    public ApiError buildUsernameApiError() {
        ApiError apiError = new ApiError();
        apiError.setMessage(ExceptionMessages.USERNAME_EMPTY.toString());
        apiError.setStatus(HttpStatus.BAD_REQUEST.value());

        return apiError;
    }

    public ApiError buildApiError(HttpMediaTypeNotSupportedException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getBody().getDetail());
        apiError.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());

        return apiError;
    }

    public ApiError buildApiError(HttpMediaTypeNotAcceptableException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getBody().getDetail());
        apiError.setStatus(HttpStatus.NOT_ACCEPTABLE.value());

        return apiError;
    }

    public ApiError buildApiError(JsonParsingException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.BAD_REQUEST.value());

        return apiError;
    }
}
