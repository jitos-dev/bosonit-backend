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
public class TeacherOutputDto {

    @JsonProperty(value = "id_teacher")
    private String idTeacher;

    private String comments;

    private Branch branch;

    private PersonOutputDto person;

    @JsonProperty(value = "student_list")
    private List<StudentSimpleOutputDto> studentList;
}
