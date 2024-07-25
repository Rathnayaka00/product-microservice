package com.example.producutmicroservice.controller;

import com.example.producutmicroservice.exception.ErrorResponse;
import com.example.producutmicroservice.exception.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

public abstract class AbstractController {

    protected ResponseEntity<ErrorResponse> handleNotFoundException(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    protected ResponseEntity<ErrorResponse> handleInternalServerError(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected <T> ResponseEntity<SuccessResponse<T>> createSuccessResponse(T body, String message, HttpStatus status) {
        SuccessResponse<T> successResponse = new SuccessResponse<>(status, message, body);
        return new ResponseEntity<>(successResponse, status);
    }

    protected <T> ResponseEntity<SuccessResponse<T>> createSuccessResponse(T body, HttpStatus status) {
        return createSuccessResponse(body, "Request was successful", status);
    }

}