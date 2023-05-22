package com.bosonit.garciajuanjo.Block11uploaddownloadfiles.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class CustomError {

    private HttpStatus httpStatus;
    private Integer code;
    private String message;
}
