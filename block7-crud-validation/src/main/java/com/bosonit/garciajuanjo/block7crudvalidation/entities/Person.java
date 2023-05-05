package com.bosonit.garciajuanjo.block7crudvalidation.entities;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonOutputDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "persons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(generator = "myGenerator")
    @GenericGenerator(name = "myGenerator", strategy = "com.bosonit.garciajuanjo.block7crudvalidation.utils.MyIdentifierGenerator")
    @Column(name = "id_person", nullable = false, unique = true)
    @JsonProperty(value = "id_person")
    private String idPerson;
    @Column(name = "usuario", nullable = false, length = 10)
    private String user;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    private String surname;
    @Column(name = "company_email", nullable = false)
    private String companyEmail;
    @Column(name = "personal_email", nullable = false)
    private String personalEmail;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private Boolean active;
    @Column(name = "created_date", nullable = false)
    private Date createdDate;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "termination_date")
    private Date terminationDate;

/*
    public Person(String user,
                  String password,
                  String name,
                  String surname,
                  String companyEmail,
                  String personalEmail,
                  String city,
                  Boolean active,
                  Date createdDate,
                  String imageUrl,
                  Date terminationDate) {
        this.idPerson = generateId();
        this.user = user;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.companyEmail = companyEmail;
        this.personalEmail = personalEmail;
        this.city = city;
        this.active = active;
        this.createdDate = createdDate;
        this.imageUrl = imageUrl;
        this.terminationDate = terminationDate;
    }

    public Person(){
        this.idPerson = generateId();
    }
*/

    public Person(PersonInputDto personInputDto) {
        this.idPerson = personInputDto.getIdPerson();
        this.user = personInputDto.getUser();
        this.password = personInputDto.getPassword();
        this.name = personInputDto.getName();
        this.surname = personInputDto.getSurname();
        this.companyEmail = personInputDto.getCompanyEmail();
        this.personalEmail = personInputDto.getPersonalEmail();
        this.city = personInputDto.getCity();
        this.active = personInputDto.getActive();
        this.createdDate = personInputDto.getCreatedDate();
        this.imageUrl = personInputDto.getImageUrl();
        this.terminationDate = personInputDto.getTerminationDate();
    }

    public PersonOutputDto personToPersonOutputDto() {
        return new PersonOutputDto(
                this.idPerson,
                this.user,
                this.password,
                this.name,
                this.surname,
                this.companyEmail,
                this.personalEmail,
                this.city,
                this.active,
                this.createdDate,
                this.imageUrl,
                this.terminationDate);
    }

}

