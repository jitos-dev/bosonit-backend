package com.bosonit.garciajuanjo.Block11uploaddownloadfiles.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class FileOutputDto {

    @JsonProperty("file_id")
    private Long fileId;
    private String filename;
    @JsonProperty("upload_date")
    private Date uploadDate;
    private String category;
}
