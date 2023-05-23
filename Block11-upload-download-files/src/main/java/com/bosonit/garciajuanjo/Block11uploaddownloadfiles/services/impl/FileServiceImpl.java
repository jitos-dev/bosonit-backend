package com.bosonit.garciajuanjo.Block11uploaddownloadfiles.services.impl;

import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.entities.FileOutputDto;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.exceptions.FileAlreadyExistException;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.exceptions.FileNotFoundException;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.services.FileService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
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

    private Path directory;

    @PostConstruct
    void initDirectory() {
        directory = Paths.get(pathFiles);
    }

    @Override
    public void store(MultipartFile file) throws IOException, FileAlreadyExistException {
        //Compruebo que no exista el fichero antes de guardarlo porque si no da error
        File fileStore = new File(directory.toFile() + File.separator + file.getOriginalFilename());
        if (fileStore.exists())
            throw new FileAlreadyExistException("The file " + file.getOriginalFilename() + " already exist in the directory " + directory);

        /*Con la función copy lo que hacemos es primero pasar el InputStream que vamos a copiar en la carpeta. Luego
         * con el segundo parámetro decimos que lo copiamos en el directorio (directory) con el nombre original que
         * nos viene en el MultipartFile*/
        Files.copy(file.getInputStream(), directory.resolve(Objects.requireNonNull(file.getOriginalFilename())));
    }

    @Override
    public void store(List<MultipartFile> files) throws IOException, FileAlreadyExistException {
        for (MultipartFile file : files) {
            this.store(file);
        }
    }

    @Override
    public List<FileOutputDto> loadAll() throws IOException {
        try (Stream<Path> paths = Files.walk(directory)) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(directory::relativize)
                    .map(path -> {
                        String fileName = path.getFileName().toString();
                        String url = directory + "/" + fileName;

                        return new FileOutputDto(fileName, url);
                    })
                    .toList();
        }
    }

    @Override
    public Path load(String filename) {
        return directory.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        Path absolutePath = Paths.get(pathFiles, filename);

        Resource resource = new PathResource(absolutePath);

        if (!resource.exists() || !resource.isReadable())
            throw new FileNotFoundException("The file does not exist or is not readable");

        return resource;
    }

    @Override
    public void deleteAll() throws IOException {
        try (Stream<Path> paths = Files.walk(directory)) {
            List<Path> files = paths
                    .filter(Files::isRegularFile)
                    .toList();

            for (Path file :files) {
                Files.delete(file);
            }
        }
    }
}
