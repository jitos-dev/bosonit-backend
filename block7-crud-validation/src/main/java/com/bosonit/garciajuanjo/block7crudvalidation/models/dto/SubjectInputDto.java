package com.bosonit.garciajuanjo.block7crudvalidation.models.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SubjectInputDto {

    private String subjectId;
    private String subjectName;
    private String comments;
    private Date initialDate;
    private Date finishDate;
}
