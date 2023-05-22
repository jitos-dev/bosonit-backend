package com.bosonit.garciajuanjo.Block11uploaddownloadfiles.controllers;

import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.entities.Fichero;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.services.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@RestController
@AllArgsConstructor
public class FileController {

    private FileService fileService;

    @PostMapping(value = "upload/{type}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            @PathVariable("type") String type) throws IOException {

        //Guardamos el file
        fileService.store(file);

        String message = "The file saved successfully";
        Fichero fichero = new Fichero(message, type, file.getOriginalFilename(), new Date(), file);

        return ResponseEntity.ok().body(fichero);
    }



}
