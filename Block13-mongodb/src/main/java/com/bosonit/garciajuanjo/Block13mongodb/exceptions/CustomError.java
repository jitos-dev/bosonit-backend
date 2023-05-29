package com.bosonit.garciajuanjo.Block13mongodb.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomError {

    private Date timestamp;
    private Integer httpCode;
    private String message;
}
