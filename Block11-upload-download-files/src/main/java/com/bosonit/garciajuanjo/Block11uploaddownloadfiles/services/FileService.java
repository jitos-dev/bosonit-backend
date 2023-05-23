package com.bosonit.garciajuanjo.Block11uploaddownloadfiles.services;

import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.entities.FileOutputDto;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.exceptions.FileAlreadyExistException;
import com.bosonit.garciajuanjo.Block11uploaddownloadfiles.exceptions.FileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;

public interface FileService {

    void store(MultipartFile file) throws IOException, FileAlreadyExistException;

    void store(List<MultipartFile> files) throws IOException, FileAlreadyExistException;

    List<FileOutputDto> loadAll() throws IOException;

    Path load(String filename) throws MalformedURLException;

    Resource loadAsResource(String filename) throws MalformedURLException, FileNotFoundException;

    void deleteAll() throws IOException;
}
