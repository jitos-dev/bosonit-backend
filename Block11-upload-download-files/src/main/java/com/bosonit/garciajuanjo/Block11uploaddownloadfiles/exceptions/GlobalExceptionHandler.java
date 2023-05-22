package com.bosonit.garciajuanjo.Block11uploaddownloadfiles.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.net.MalformedURLException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<CustomError> handleException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(
                new CustomError(status,
                        status.value(),
                        "Error type Exception: " + ex.getMessage()),
                status);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<CustomError> handleIOException(IOException ioe) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(
                new CustomError(status,
                        status.value(),
                        "Error type IOException: " + ioe.getMessage()),
                status);
    }

    @ExceptionHandler(FileExistException.class)
    public ResponseEntity<CustomError> handleFileExistException(FileExistException fee) {
        HttpStatus status = HttpStatus.CONFLICT;

        return new ResponseEntity<>(
                new CustomError(status,
                        status.value(),
                        "Error type FileExistException: " + fee.getMessage()),
                status);
    }

    @ExceptionHandler(MalformedURLException.class)
    public ResponseEntity<CustomError> handleMalformedURLException(MalformedURLException mue) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(
                new CustomError(status,
                        status.value(),
                        "Error type MalformedURLException: " + mue.getMessage()),
                status);
    }
}
