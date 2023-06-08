package com.bosonit.garciajuanjo.Block13mongodb.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnprocessableEntityException extends RuntimeException{

    private final HttpStatus httpStatus;
    public UnprocessableEntityException(String message) {
        super(message);
        this.httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
    }

    public UnprocessableEntityException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
