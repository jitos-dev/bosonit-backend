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
public class PersonCompleteOutputDto {

    @JsonProperty(value = "person")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private PersonOutputDto person;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "student_subjects")
    private StudentAndSubjectsOutputDto studentAndSubjectsOutputDto;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "teacher")
    private TeacherOutputDto teacherOutputDto;

    @JsonProperty(value = "student_subjects_list")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<StudentAndSubjectsOutputDto> studentAndSubjectsOutputDtoList = new ArrayList<>();

    @JsonProperty(value = "subjects")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<StudentSubjectOutputDto> studentsSubjectOutputDto;
}
