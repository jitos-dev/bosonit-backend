package com.bosonit.garciajuanjo.Block11uploaddownloadfiles.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileComponent implements BasicFileComponent{

    @Value("${path_files}")
    private String pathFiles;

    @Override
    public Path getPathFiles() {
        return Paths.get(pathFiles);
    }

    @Override
    public void setPathFiles(String pathFiles) {
        this.pathFiles = pathFiles;
    }
}
