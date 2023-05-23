package com.bosonit.garciajuanjo.Block11uploaddownloadfiles.services.impl;

import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.components.BasicFileComponent;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.entities.FieldSearch;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.entities.FileEntity;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.entities.FileInputDto;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.entities.FileOutputDto;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.exceptions.FileAlreadyExistException;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.exceptions.FileNotFoundException;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.repositories.FileRepository;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    BasicFileComponent fileComponent;

    @Autowired
    private FileRepository fileRepository;

    @Override
    public Optional<FileOutputDto> store(MultipartFile file, String category) throws IOException, FileAlreadyExistException {
        //Compruebo que no exista el fichero antes de guardarlo porque si no da error
        File fileStore = new File(fileComponent.getPathFiles().toFile() + File.separator + file.getOriginalFilename());
        if (fileStore.exists())
            throw new FileAlreadyExistException("The file " + file.getOriginalFilename() +
                    " already exist in the directory " + fileComponent.getPathFiles());

        /*Con la función copy lo que hacemos es primero pasar el InputStream que vamos a copiar en la carpeta. Luego
         * con el segundo parámetro decimos que lo copiamos en el directorio (directory) con el nombre original que
         * nos viene en el MultipartFile*/
        Files.copy(file.getInputStream(), fileComponent.getPathFiles().resolve(Objects.requireNonNull(file.getOriginalFilename())));

        FileInputDto inputDto = new FileInputDto(null, file.getOriginalFilename(), new Date(), category);
        FileEntity entity = fileRepository.save(new FileEntity(inputDto));

        return Optional.of(entity.fileEntityToFileOutputDto());
    }

    @Override
    public List<FileOutputDto> loadAll() throws IOException {
        List<FileOutputDto> entitiesOutputDto;

        try (Stream<Path> paths = Files.walk(fileComponent.getPathFiles())) {

            //Obtenemos la lista con los nombres de los ficheros guardados en el disco duro
            List<String> namesFiles = paths
                    .filter(Files::isRegularFile)
                    .map(fileComponent.getPathFiles()::relativize)
                    .map(Path::toString)
                    .toList();

            /*Obtenemos la lista con las Entities de la bd y las filtramos para solo devolver las que esten en la ruta*/
            List<FileEntity> entities = fileRepository.findAll();
            List<FileEntity> filterEntities = entities.stream().filter(entity -> namesFiles.contains(entity.getFilename())).toList();

            entitiesOutputDto = filterEntities.stream().map(FileEntity::fileEntityToFileOutputDto).toList();
        }

        return entitiesOutputDto;
    }

    @Override
    public Path load(String filename) {
        return fileComponent.getPathFiles().resolve(filename);
    }

    @Override
    public Resource loadAsResource(String value, FieldSearch fieldSearch) {
        Resource resource = null;

        switch (fieldSearch) {
            case ID -> {
                FileEntity entity = fileRepository.findById(Long.parseLong(value))
                        .orElseThrow(() -> new FileNotFoundException("The id does not correspond to any file"));

                Path absolutePath = Paths.get(fileComponent.getPathFiles().toString(), entity.getFilename());
                resource = new PathResource(absolutePath);
            }
            case FILENAME -> {
                Path absolutePath = Paths.get(fileComponent.getPathFiles().toString(), value);
                resource = new PathResource(absolutePath);
            }
        }

        if (resource == null || !resource.exists() || !resource.isReadable())
            throw new FileNotFoundException("The file does not exist or is not readable");

        return resource;
    }

    @Override
    public void deleteAll() throws IOException {
        try (Stream<Path> paths = Files.walk(fileComponent.getPathFiles())) {
            List<Path> files = paths
                    .filter(Files::isRegularFile)
                    .toList();

            for (Path file :files) {
                Files.delete(file);
            }

            fileRepository.deleteAll();
        }
    }

    @Override
    public void setPath(String path) throws IOException {
        File file = new File(path);

        //si el directorio no existe lo creamos
        if (!file.exists())
            Files.createDirectory(Paths.get(file.toURI()));

        fileComponent.setPathFiles(path);
    }
}
