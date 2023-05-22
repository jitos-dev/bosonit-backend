package com.bosonit.garciajuanjo.Block11uploaddownloadfiles.services.impl;

import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.controllers.FileController;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.entities.FileOutputDto;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.exceptions.FileExistException;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.services.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

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
        for (MultipartFile file : files) {
            this.store(file);
        }
    }

    @Override
    public List<FileOutputDto> loadAll() throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(pathFiles))) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(Paths.get(pathFiles)::relativize)
                    .map(path -> {
                        String fileName = path.getFileName().toString();
                        String url = pathFiles + "/" + fileName;

                        return new FileOutputDto(fileName, url);
                    })
                    .toList();
        }
    }

    @Override
    public Path load(String filename) {
        return Paths.get(pathFiles).toAbsolutePath().resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) throws MalformedURLException {
        return new UrlResource(this.load(filename).toUri());
    }

    @Override
    public void deleteAll() throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(pathFiles))) {
            List<Path> files = paths
                    .filter(Files::isRegularFile)
                    .toList();

            for (Path file :files) {
                Files.delete(file);
            }
        }
    }
}
