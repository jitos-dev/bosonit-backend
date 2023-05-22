package com.bosonit.garciajuanjo.Block11uploaddownloadfiles.services;

import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.entities.Fichero;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.exceptions.FileExistException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface FileService {

    void store(MultipartFile file) throws IOException, FileExistException;

    void store(List<MultipartFile> files) throws IOException, FileExistException;

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();
}
