package com.bosonit.garciajuanjo.block7crudvalidation.models.dto;

import com.bosonit.garciajuanjo.block7crudvalidation.models.Branch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherInputDto {

    private String idTeacher;
    private String comments;
    private Branch branch;
    private String personId;
}
