package com.kapusniak.tomasz.atiperatask.service;

import com.kapusniak.tomasz.atiperatask.exception.ExceptionMessages;
import com.kapusniak.tomasz.atiperatask.exception.HeaderException;
import com.kapusniak.tomasz.atiperatask.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.client.HttpClientErrorException;

import static com.kapusniak.tomasz.atiperatask.exception.ExceptionMessages.HEADER_NOT_ACCEPTABLE;
import static com.kapusniak.tomasz.atiperatask.exception.ExceptionMessages.HEADER_NOT_SUPPORTED;

@Service
public class ApiErrorService {

    public ApiError buildApiError(HttpClientErrorException.NotFound ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ExceptionMessages.USERNAME_NOT_FOUND.toString());
        apiError.setStatus((HttpStatus) ex.getStatusCode());

        return apiError;
    }
    public ApiError buildUsernameApiError() {
        ApiError apiError = new ApiError();
        apiError.setMessage(ExceptionMessages.USERNAME_EMPTY.toString());
        apiError.setStatus(HttpStatus.BAD_REQUEST);

        return apiError;
    }
    public ApiError buildHeaderApiError(HeaderException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(HEADER_NOT_SUPPORTED + ex.getHeader());
        apiError.setStatus(HttpStatus.NOT_ACCEPTABLE);

        return apiError;
    }
    public ApiError buildHeaderApiError(HttpMediaTypeNotSupportedException ex) {
        ApiError apiError = new ApiError();
//        apiError.setMessage(HEADER_NOT_SUPPORTED + ex.getHeaders().toString());
        apiError.setMessage(ex.getBody().getDetail());
        apiError.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE);

        return apiError;
    }
    public ApiError buildHeaderApiError(HttpMediaTypeNotAcceptableException ex) {
        ApiError apiError = new ApiError();
        apiError.setMessage(ex.getBody().getDetail());
        apiError.setStatus(HttpStatus.NOT_ACCEPTABLE);

        return apiError;
    }

    public void validateHeader(String header){
        if (!header.equals(MediaType.APPLICATION_JSON_VALUE)) {
            throw new HeaderException(HEADER_NOT_ACCEPTABLE.toString(), header);
        }

    }
}
