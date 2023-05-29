package com.bosonit.garciajuanjo.Block13mongodb.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.ParseException;
import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CustomError> handleEntityNotFoundException(EntityNotFoundException ex) {
        CustomError error = new CustomError();
        error.setTimestamp(new Date());
        error.setHttpCode(HttpStatus.NOT_FOUND.value());

        String message = ex.getMessage() == null ? "No record exist for the request resource" : ex.getMessage();
        error.setMessage(message);

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<CustomError> handleUnprocessableEntityException(UnprocessableEntityException exception) {
        CustomError error = new CustomError();
        error.setTimestamp(new Date());
        error.setHttpCode(HttpStatus.NO_CONTENT.value());
        error.setMessage(exception.getMessage());

        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<CustomError> handleParseException(ParseException pe) {
        CustomError customError = new CustomError();
        customError.setTimestamp(new Date());
        customError.setHttpCode(HttpStatus.BAD_REQUEST.value());
        customError.setMessage(pe.getMessage());

        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Este metodo se ejecuta cuando salta una excepcion si utilizamos la anotación @Valid y luego en la entidad
     * por ejemplo la anotacion @NotNull
     * @param ex MethodArgumentNotValidException
     * @return ResponseEntity<CustomError>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        HttpStatusCode statusCode = ex.getStatusCode();
        ObjectError error = ex.getAllErrors().get(0);
        String message = error.getDefaultMessage();

        CustomError customError = new CustomError();
        customError.setTimestamp(new Date());
        customError.setHttpCode(statusCode.value());
        customError.setMessage(message);

        return new ResponseEntity<>(customError, Objects.requireNonNull(HttpStatus.resolve(statusCode.value())));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CustomError> handleRuntimeException(RuntimeException rte) {
        CustomError customError = new CustomError();
        customError.setTimestamp(new Date());
        customError.setHttpCode(HttpStatus.BAD_REQUEST.value());
        customError.setMessage(rte.getMessage());

        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomError> handleException(Exception ex) {
        CustomError customError = new CustomError();
        customError.setTimestamp(new Date());
        customError.setHttpCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        customError.setMessage(ex.getMessage());

        return new ResponseEntity<>(customError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
