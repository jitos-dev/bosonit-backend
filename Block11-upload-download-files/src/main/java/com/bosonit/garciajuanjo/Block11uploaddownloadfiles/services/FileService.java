package com.bosonit.garciajuanjo.Block11uploaddownloadfiles.services;

import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.entities.Fichero;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface FileService {

    void store(MultipartFile file) throws IOException;

    void store(List<MultipartFile> files) throws IOException;

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();
}
