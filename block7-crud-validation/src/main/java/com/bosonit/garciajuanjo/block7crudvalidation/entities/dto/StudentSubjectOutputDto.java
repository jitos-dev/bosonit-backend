package com.bosonit.garciajuanjo.block7crudvalidation.entities.dto;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.Subject;
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
public class StudentSubjectOutputDto {

    @JsonProperty(value = "id_subject")
    private String idStudentSubject;
    private Subject subject;
    private String comments;
    @JsonProperty(value = "initial_date")
    private Date initialDate;
    @JsonProperty(value = "finish_date")
    private Date finishDate;
    private StudentOutputDto student;
}
