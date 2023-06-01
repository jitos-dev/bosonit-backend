package com.bosonit.garciajuanjo.block7crudvalidation.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonCompleteOutputDto {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    PersonOutputDto person;

    @JsonProperty(value = "student")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private StudentOutputDto student;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "teacher")
    private TeacherOutputDto teacher;
}
