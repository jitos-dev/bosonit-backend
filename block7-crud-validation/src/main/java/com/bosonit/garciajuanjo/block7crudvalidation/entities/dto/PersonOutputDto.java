package com.bosonit.garciajuanjo.block7crudvalidation.entities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonOutputDto {

    @JsonProperty(value = "id_persona")
    private String idPerson;
    @JsonProperty(value = "usuario")
    private String user;
    private String password;
    private String name;
    private String surname;
    @JsonProperty(value = "company_email")
    private String companyEmail;
    @JsonProperty(value = "personal_email")
    private String personalEmail;
    private String city;
    private Boolean active;
    @JsonProperty(value = "created_date")
    private Date createdDate;
    @JsonProperty(value = "imagen_url")
    private String imageUrl;
    @JsonProperty(value = "termination_date")
    private Date terminationDate;

}
