package dev.ailuruslabs.ohmyrest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Mono<ResponseEntity<ErrorResponse>> buildErrorResponse(HttpStatus status, String message) {
        return Mono.fromSupplier(
                () -> new ErrorResponse(
                    status.value(),
                    message,
                    Instant.now().toEpochMilli()
                )
            )
            .map(errorResponse -> new ResponseEntity<>(errorResponse, status));
    }

    private Mono<ResponseEntity<ErrorResponseWithFields>> buildErrorResponseWithFields(List<FieldError> fieldErrors) {
        return Flux.fromIterable(fieldErrors)
            .map(fieldError -> new FieldAndMessage(fieldError.getField(), fieldError.getDefaultMessage()))
            .collectList()
            .map(
                fieldErrorsList -> new ResponseEntity<>(new ErrorResponseWithFields(
                    HttpStatus.BAD_REQUEST.value(),
                    fieldErrorsList,
                    Instant.now().toEpochMilli()
                ), HttpStatus.BAD_REQUEST)
            );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleIllegalArgument(IllegalArgumentException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponseWithFields>> handleWebExchangeBind(WebExchangeBindException e) {
        return buildErrorResponseWithFields(e.getFieldErrors());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleResponseStatusException(ResponseStatusException e) {
        return buildErrorResponse(HttpStatus.resolve(e.getStatusCode().value()), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleRuntimeException(Exception e) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + e.getMessage());
    }

}
