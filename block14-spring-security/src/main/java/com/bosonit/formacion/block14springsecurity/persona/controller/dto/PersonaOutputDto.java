package com.bosonit.formacion.block14springsecurity.persona.controller.dto;

import com.bosonit.formacion.block14springsecurity.persona.domain.Persona;
import lombok.Getter;

import java.util.Date;

@Getter
public class PersonaOutputDto {
    private Integer idPersona;
    private String usuario;
    private String name;
    private String surname;
    private String companyEmail;
    private String personalEmail;
    private String city;
    private Boolean active;
    private Date createdDate;
    private String imagenUrl;
    private Date terminationDate;
    private Boolean admin;



    //Constructor con persona por Parámetro.
    public PersonaOutputDto(Persona persona) {
        this.idPersona = persona.getIdPersona();
        this.usuario = persona.getUsuario();
        this.name = persona.getName();
        this.surname = persona.getSurname();
        this.companyEmail = persona.getCompanyEmail();
        this.personalEmail = persona.getPersonalEmail();
        this.city = persona.getCity();
        this.active = persona.getActive();
        this.createdDate = persona.getCreatedDate();
        this.imagenUrl = persona.getImagenUrl();
        this.terminationDate = persona.getTerminationDate();
        this.admin=persona.getAdmin();
    }
}
