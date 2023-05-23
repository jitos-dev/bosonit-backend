package com.bosonit.garciajuanjo.Block11uploaddownloadfiles.exceptions;

public class FileNotFoundException extends RuntimeException{

    private String message;

    public FileNotFoundException(String message) {
        super(message);
    }
}
