package com.bosonit.garciajuanjo.block7crudvalidation.entities.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class PersonAllOutputDto {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    PersonOutputDto person;

    @JsonProperty(value = "student")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private StudentSimpleOutputDto student;

    @JsonProperty(value = "subjects")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<StudentSubjectSimpleOutputDto> subjects;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "teacher")
    private TeacherOutputDto teacherOutputDto;
}
