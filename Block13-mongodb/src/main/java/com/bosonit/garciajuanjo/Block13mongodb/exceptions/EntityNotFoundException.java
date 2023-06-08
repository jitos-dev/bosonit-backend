package com.bosonit.garciajuanjo.Block13mongodb.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EntityNotFoundException extends RuntimeException{

    private final HttpStatus httpStatus;
    public EntityNotFoundException() {
        super();
        this.httpStatus = HttpStatus.NOT_FOUND;
    }

    public EntityNotFoundException(String message) {
        super(message);
        this.httpStatus = HttpStatus.NOT_FOUND;
    }

}
