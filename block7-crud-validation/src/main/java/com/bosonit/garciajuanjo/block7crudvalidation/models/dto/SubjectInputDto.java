package com.bosonit.garciajuanjo.block7crudvalidation.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectInputDto {

    private String subjectId;
    private String subjectName;
    private String comments;
    private Date initialDate;
    private Date finishDate;
}
