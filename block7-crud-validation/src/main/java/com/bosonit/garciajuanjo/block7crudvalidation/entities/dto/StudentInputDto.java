package com.bosonit.garciajuanjo.block7crudvalidation.entities.dto;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.Branch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentInputDto {

    private Integer numHoursWeek;
    private String comments;
    private Branch branch;
    private String personId;
    private String teacherId;
}
