package com.bosonit.garciajuanjo.Block11uploaddownloadfiles.controllers;

import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.entities.FileEntity;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.entities.FileOutputDto;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.exceptions.FileAlreadyExistException;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.services.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class FileController {

    private FileService fileService;

    @PostMapping(value = "upload/{type}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public FileOutputDto upload(
            @RequestParam("file") MultipartFile file,
            @PathVariable("type") String type) throws IOException, FileAlreadyExistException {

        String originalName = file.getOriginalFilename();

        if (originalName == null || !originalName.endsWith(type))
            throw new RuntimeException("The chosen type is not same type of the file");

        //Guardamos el file
        return fileService.store(file, type).orElseThrow();
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> allFiles() throws IOException {
        return ResponseEntity.ok().body(fileService.loadAll());
    }

    @GetMapping("/")
    public ResponseEntity<?> download(@RequestParam String name) throws MalformedURLException {
        Resource resource = fileService.loadAsResource(name);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/all")
    public ResponseEntity<String> deleteAll() throws IOException {
        fileService.deleteAll();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("The files deleted successfully");
    }
}
