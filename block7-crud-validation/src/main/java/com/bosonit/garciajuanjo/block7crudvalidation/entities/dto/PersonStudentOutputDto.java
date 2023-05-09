package com.bosonit.garciajuanjo.block7crudvalidation.entities.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonStudentOutputDto {

    @JsonProperty(value = "person")
    private PersonOutputDto personOutputDto;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "student_and_subject")
    private StudentAndSubjectOutputDto studentAndSubjectOutputDto;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "teacher")
    private TeacherOutputDto teacherOutputDto;

    @JsonProperty(value = "students_and_subject")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<StudentAndSubjectOutputDto> studentAndSubjectOutputDtoList = new ArrayList<>();

    @JsonProperty(value = "students_subject")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<StudentSubjectOutputDto> studentsSubjectOutputDto;
}
