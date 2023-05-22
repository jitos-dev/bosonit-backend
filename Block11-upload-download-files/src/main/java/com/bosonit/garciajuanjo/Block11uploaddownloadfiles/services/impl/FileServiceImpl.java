package com.bosonit.garciajuanjo.Block11uploaddownloadfiles.services.impl;

import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.services.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileService {

    @Value("${path_files}")
    private String pathFiles;

    @Override
    public void store(MultipartFile file) throws IOException{
        Path directory = Paths.get(pathFiles).toAbsolutePath();

        /*Con la función copy lo que hacemos es primero pasar el InputStream que vamos a copiar en la carpeta. Luego
        * con el segundo parámetro decimos que lo copiamos en el directorio (directory) con el nombre original que
        * nos viene en el MultipartFile*/
        Files.copy(file.getInputStream(), directory.resolve(Objects.requireNonNull(file.getOriginalFilename())));

    }

    @Override
    public void store(List<MultipartFile> files) throws IOException{
        for (MultipartFile file :files) {
            this.store(file);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public Path load(String filename) {
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) {
        return null;
    }

    @Override
    public void deleteAll() {

    }
}
