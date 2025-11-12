package com.cosmocats.marketplace;

import com.cosmocats.marketplace.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Validation Error");
        problemDetail.setProperty("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> String.format("Field '%s': %s", error.getField(), error.getDefaultMessage()))
            .collect(Collectors.joining("; "));
        problemDetail.setDetail("Validation failed for object '" + ex.getBindingResult().getObjectName() + "': " + message);
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        problemDetail.setType(URI.create("https://cosmocats.com/errors/validation-error"));
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleProductNotFound(ProductNotFoundException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Product Not Found");
        problemDetail.setProperty("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        problemDetail.setType(URI.create("https://cosmocats.com/errors/product-not-found"));
        return new ResponseEntity<>(problemDetail, HttpStatus.NOT_FOUND);
    }
}