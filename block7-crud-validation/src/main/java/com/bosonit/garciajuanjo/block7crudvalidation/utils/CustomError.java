package com.bosonit.garciajuanjo.block7crudvalidation.utils;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomError {

    private Date timestamp;
    private Integer httpCode;
    private String message;
}
