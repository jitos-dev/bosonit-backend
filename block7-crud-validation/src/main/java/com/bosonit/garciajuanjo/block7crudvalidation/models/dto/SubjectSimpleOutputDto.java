package com.bosonit.garciajuanjo.block7crudvalidation.models.dto;

import com.bosonit.garciajuanjo.block7crudvalidation.models.SubjectName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectSimpleOutputDto {

    @JsonProperty(value = "id_subject")
    private String idStudentSubject;
    private SubjectName subjectName;
    private String comments;
    @JsonProperty(value = "initial_date")
    private Date initialDate;
    @JsonProperty(value = "finish_date")
    private Date finishDate;
}
