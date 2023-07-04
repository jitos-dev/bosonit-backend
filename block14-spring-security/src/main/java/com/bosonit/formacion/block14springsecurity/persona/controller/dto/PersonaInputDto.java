package com.bosonit.formacion.block14springsecurity.persona.controller.dto;

import lombok.Getter;

import java.util.Date;

@Getter
public class PersonaInputDto {
    private String idPersona;
    private String usuario;
    private String password;
    private String name;
    private String surname;
    private String companyEmail;
    private String personalEmail;
    private String city;
    private Boolean active;
    private String imagenUrl;
    private Date terminationDate;
    private Boolean admin;

}
