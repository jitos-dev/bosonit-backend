package com.bosonit.garciajuanjo.Block11uploaddownloadfiles.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
public class Fichero {

    private static Long fileId = 0L;

    private Long id;
    private String message;
    private String type;
    private String name;
    private Date uploadDate;
    private MultipartFile file;

    public Fichero(String message, String type, String name, Date uploadDate, MultipartFile file) {
        fileId++;
        this.id = fileId;
        this.message = message;
        this.type = type;
        this.name = name;
        this.uploadDate = uploadDate;
        this.file = file;
    }
}
