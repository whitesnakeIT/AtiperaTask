package com.kapusniak.tomasz.atiperatask.handler;

import com.kapusniak.tomasz.atiperatask.exception.JsonParsingException;
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

        return new ResponseEntity<>(apiError, HttpStatusCode.valueOf(apiError.getStatus()));
    }

    @ExceptionHandler(UsernameEmptyNameException.class)
    public ResponseEntity<ApiError> handleUserEmptyName() {
        ApiError apiError = apiErrorService.buildUsernameApiError();

        return new ResponseEntity<>(apiError, HttpStatusCode.valueOf(apiError.getStatus()));
    }

    @ExceptionHandler(JsonParsingException.class)
    public ResponseEntity<ApiError> handleHeaderException(JsonParsingException ex) {
        ApiError apiError = apiErrorService.buildApiError(ex);

        return new ResponseEntity<>(apiError, HttpStatusCode.valueOf(apiError.getStatus()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiError apiError = apiErrorService.buildApiError(ex);

        return new ResponseEntity<>(apiError, createJsonResponseHeaders(), HttpStatusCode.valueOf(apiError.getStatus()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiError apiError = apiErrorService.buildApiError(ex);

        return new ResponseEntity<>(apiError, createJsonResponseHeaders(), HttpStatusCode.valueOf(apiError.getStatus()));
    }

    private HttpHeaders createJsonResponseHeaders() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return responseHeaders;
    }
}
