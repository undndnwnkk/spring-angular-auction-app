package com.auction.biddingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IncorrectLotInformationException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectLotInformationException(IncorrectLotInformationException ex) {
        ErrorResponse error = ErrorResponse.create(
                ex,
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IncorrectBidInformationException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectBidInformationException(IncorrectBidInformationException ex) {
        ErrorResponse error = ErrorResponse.create(
                ex,
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException ex) {
        ErrorResponse error = ErrorResponse.create(
                ex,
                HttpStatus.CONFLICT,
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
