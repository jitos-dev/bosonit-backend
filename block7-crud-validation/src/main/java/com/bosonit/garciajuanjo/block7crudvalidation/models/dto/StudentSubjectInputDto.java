package com.bosonit.garciajuanjo.block7crudvalidation.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentSubjectInputDto {

    private String studentId;
    private String subjectId;
}
