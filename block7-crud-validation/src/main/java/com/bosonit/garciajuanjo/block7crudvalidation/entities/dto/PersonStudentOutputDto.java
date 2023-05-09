package com.bosonit.garciajuanjo.block7crudvalidation.entities.dto;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.StudentSubject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonStudentOutputDto {

    @JsonProperty(value = "person")
    private PersonOutputDto personOutputDto;
    @JsonProperty(value = "student")
    private StudentOutputDto studentOutputDto;
    @JsonProperty(value = "teacher")
    private TeacherOutputDto teacherOutputDto;
    @JsonProperty(value = "studentsSubject")
    private List<StudentSubjectOutputDto> studentsSubjectOutputDto;
}
