package com.bosonit.garciajuanjo.Block11uploaddownloadfiles.controllers;

import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.entities.FieldSearch;
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

@RestController
@RequestMapping("file")
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

    @GetMapping
    public ResponseEntity<?> allFiles() throws IOException {
        return ResponseEntity.ok().body(fileService.loadAll());
    }

    @GetMapping("by")
    public ResponseEntity<?> downloadBy(
            @RequestParam(name = "filename", required = false) String filename,
            @RequestParam(name = "id", required = false) String id) throws MalformedURLException {

        Resource resource = null;

        if (id != null)
            resource = fileService.loadAsResource(id, FieldSearch.ID);

        if (filename != null)
            resource = fileService.loadAsResource(filename, FieldSearch.FILENAME);

        if (resource == null)
            throw new RuntimeException("The resource is null");
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("setpath")
    public ResponseEntity<?> setPath(@RequestParam String path) throws IOException {
        fileService.setPath(path);

        return ResponseEntity.ok("The path to save files has been changed successfully");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAll() throws IOException {
        fileService.deleteAll();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("The files deleted successfully");
    }
}
