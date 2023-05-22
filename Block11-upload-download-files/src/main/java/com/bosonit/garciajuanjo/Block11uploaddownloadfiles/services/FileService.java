package com.bosonit.garciajuanjo.Block11uploaddownloadfiles.services;

import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.entities.Fichero;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.entities.FileOutputDto;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.exceptions.FileExistException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface FileService {

    void store(MultipartFile file) throws IOException, FileExistException;

    void store(List<MultipartFile> files) throws IOException, FileExistException;

    List<FileOutputDto> loadAll() throws IOException;

    Path load(String filename) throws MalformedURLException;

    Resource loadAsResource(String filename) throws MalformedURLException;

    void deleteAll() throws IOException;
}
