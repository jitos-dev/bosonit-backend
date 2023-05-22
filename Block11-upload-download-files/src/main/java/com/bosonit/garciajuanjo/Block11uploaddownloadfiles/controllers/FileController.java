package com.bosonit.garciajuanjo.Block11uploaddownloadfiles.controllers;

import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.entities.Fichero;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.exceptions.FileExistException;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.services.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

@RestController
@AllArgsConstructor
public class FileController {

    private FileService fileService;

    @PostMapping(value = "upload/{type}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            @PathVariable("type") String type) throws IOException, FileExistException {

        //Guardamos el file
        fileService.store(file);

        String message = "The file saved successfully";
        Fichero fichero = new Fichero(message, type, file.getOriginalFilename(), new Date(), file);

        return ResponseEntity.ok().body(fichero);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> allFiles() throws IOException {
        return ResponseEntity.ok().body(fileService.loadAll());
    }

    @GetMapping("/")
    public ResponseEntity<?> getByName(@RequestParam String name) throws MalformedURLException {
        Resource resource = fileService.loadAsResource(name);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }



}
