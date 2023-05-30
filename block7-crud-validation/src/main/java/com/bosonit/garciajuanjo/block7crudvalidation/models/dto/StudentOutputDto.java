package com.bosonit.garciajuanjo.block7crudvalidation.models.dto;

import com.bosonit.garciajuanjo.block7crudvalidation.models.Branch;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentOutputDto {

    @JsonProperty(value = "id_student")
    private String idStudent;

    @JsonProperty(value = "num_hours_week")
    private Integer numHoursWeek;

    private String comments;

    private Branch branch;

    private PersonOutputDto person;

    private TeacherOutputDto teacher;

    private List<SubjectSimpleOutputDto> subjects;
}
