package com.bosonit.garciajuanjo.block7crudvalidation.exceptions;

import com.bosonit.garciajuanjo.block7crudvalidation.utils.CustomError;

public class UnprocessableEntityException extends RuntimeException{

    public UnprocessableEntityException(String message) {
        super(message);
    }
}
