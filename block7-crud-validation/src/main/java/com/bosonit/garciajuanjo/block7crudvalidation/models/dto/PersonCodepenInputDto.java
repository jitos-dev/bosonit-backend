package com.bosonit.garciajuanjo.block7crudvalidation.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonCodepenInputDto {

    private String idPerson;
    @JsonProperty("usuario")
    private String user;
    private String password;
    private String name;
    private String surname;
    @JsonProperty("company_email")
    private String companyEmail;
    @JsonProperty("personal_email")
    private String personalEmail;
    private String city;
    private Boolean active;
    @JsonProperty("created_date")
    private Date createdDate;
    @JsonProperty("imagen_url")
    private String imageUrl;
    @JsonProperty("termination_date")
    private Date terminationDate;
}
