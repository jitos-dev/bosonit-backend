package com.bosonit.garciajuanjo.Block13mongodb.models.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonInputDto {

    private String idPerson;
    @NotNull(message = "The field user cannot be null")
    private String user;
    @NotNull //si no se pone message pone este: password: 'no debe ser nulo'
    private String password;
    private String name;
    private String surname;
    private String companyEmail;
    private String personalEmail;
    private String city;
    private Boolean active;
    private Date createdDate;
    private String imageUrl;
    private Date terminationDate;
}
