package com.bosonit.formacion.block14springsecurity.asignatura.controller.dto;

import lombok.Getter;

import java.util.Date;

@Getter
public class AsignaturaInputDto {
    private String nombreAsignatura;
    private String comments;
    private String branch;
    private Date finish_date;
}