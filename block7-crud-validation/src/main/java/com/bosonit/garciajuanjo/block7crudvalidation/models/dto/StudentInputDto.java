package com.bosonit.garciajuanjo.block7crudvalidation.models.dto;

import com.bosonit.garciajuanjo.block7crudvalidation.models.Branch;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class StudentInputDto {

    private Integer numHoursWeek;
    private String comments;
    private Branch branch;
    private String personId;
    private String teacherId;
}
