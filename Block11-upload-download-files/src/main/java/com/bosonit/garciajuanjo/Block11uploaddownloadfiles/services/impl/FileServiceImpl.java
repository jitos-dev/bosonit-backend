package com.bosonit.garciajuanjo.Block11uploaddownloadfiles.services.impl;

import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.exceptions.FileExistException;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.services.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
    public void store(MultipartFile file) throws IOException, FileExistException {
        Path directory = Paths.get(pathFiles).toAbsolutePath();

        //Compruebo que no exista el fichero antes de guardarlo porque si no da error
        File fileStore = new File(directory.toFile() + File.separator + file.getOriginalFilename());
        if (fileStore.exists())
            throw new FileExistException("The file " + file.getOriginalFilename() + " already exist in the directory " + directory);

        /*Con la función copy lo que hacemos es primero pasar el InputStream que vamos a copiar en la carpeta. Luego
        * con el segundo parámetro decimos que lo copiamos en el directorio (directory) con el nombre original que
        * nos viene en el MultipartFile*/
        Files.copy(file.getInputStream(), directory.resolve(Objects.requireNonNull(file.getOriginalFilename())));
    }

    @Override
    public void store(List<MultipartFile> files) throws IOException, FileExistException {
        for (MultipartFile file :files) {
            this.store(file);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public Resource load(String filename) throws MalformedURLException {
        Path file = Paths.get(pathFiles).toAbsolutePath().resolve(filename);
        return new UrlResource(file.toUri());
    }

    @Override
    public Resource loadAsResource(String filename) {
        return null;
    }

    @Override
    public void deleteAll() {

    }
}
