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
public class StudentAndSubjectOutputDto {

    @JsonProperty(value = "student")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private StudentOutputDto studentOutputDto;

    @JsonProperty(value = "subjects")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<StudentSubjectOutputDto> studentsSubjectOutputDto;
}
