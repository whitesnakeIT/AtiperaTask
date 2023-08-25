package com.kapusniak.tomasz.atiperatask.handler;

import com.kapusniak.tomasz.atiperatask.exception.HeaderException;
import com.kapusniak.tomasz.atiperatask.exception.UsernameEmptyNameException;
import com.kapusniak.tomasz.atiperatask.model.ApiError;
import com.kapusniak.tomasz.atiperatask.service.ApiErrorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ApiErrorService apiErrorService;
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<ApiError> handleUserNotFound(HttpClientErrorException.NotFound ex) {
        ApiError apiError = apiErrorService.buildApiError(ex);

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    @ExceptionHandler(UsernameEmptyNameException.class)
    public ResponseEntity<ApiError> handleUserEmptyName() {
        ApiError apiError = apiErrorService.buildUsernameApiError();

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    @ExceptionHandler(HeaderException.class)
    public ResponseEntity<ApiError> handleHeaderException(HeaderException ex) {
        ApiError apiError = apiErrorService.buildHeaderApiError(ex);

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiError apiError = apiErrorService.buildHeaderApiError(ex);
//        HttpHeaders responseHeaders = new HttpHeaders();
//
//        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiError apiError = apiErrorService.buildHeaderApiError(ex);
        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(apiError, responseHeaders, apiError.getStatus());
    }
}
