package com.bosonit.garciajuanjo.Block11uploaddownloadfiles.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "file")
@Getter
@Setter
public class FileEntity {

    @Id
    @Column(name = "file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;
    private String filename;
    @Column(name = "upload_date")
    private Date uploadDate;
    private String category;

    public FileEntity(FileInputDto inputDto){
        this.fileId = inputDto.getFileId();
        this.filename = inputDto.getFilename();
        this.uploadDate = inputDto.getUploadDate();
        this.category = inputDto.getCategory();
    }

    public FileOutputDto fileEntityToFileOutputDto() {
        return new FileOutputDto(
                this.fileId,
                this.filename,
                this.uploadDate,
                this.category,
                null
        );
    }
}
