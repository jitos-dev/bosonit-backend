package com.bosonit.garciajuanjo.block7crudvalidation.entities.dto;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.SubjectName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectOutputDto {

    @JsonProperty(value = "id_subject")
    private String idStudentSubject;
    @JsonProperty(value = "subject_name")
    private SubjectName subjectName;
    private String comments;
    @JsonProperty(value = "initial_date")
    private Date initialDate;
    @JsonProperty(value = "finish_date")
    private Date finishDate;
    private List<StudentOutputDto> students;
}
