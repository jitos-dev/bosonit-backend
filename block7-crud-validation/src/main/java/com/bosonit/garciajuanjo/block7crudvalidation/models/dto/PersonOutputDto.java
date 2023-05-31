package com.bosonit.garciajuanjo.block7crudvalidation.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonOutputDto {

    @JsonProperty(value = "id_person")
    private String idPerson;
    @JsonProperty(value = "user")
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
    @JsonProperty(value = "image_url")
    private String imageUrl;
    @JsonProperty(value = "termination_date")
    private Date terminationDate;
}
