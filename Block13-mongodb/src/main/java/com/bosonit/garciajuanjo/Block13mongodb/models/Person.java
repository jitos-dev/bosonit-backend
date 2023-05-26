package com.bosonit.garciajuanjo.Block13mongodb.models;

import com.bosonit.garciajuanjo.Block13mongodb.models.daos.PersonInputDto;
import com.bosonit.garciajuanjo.Block13mongodb.models.daos.PersonOutputDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "persons")
public class Person {

    @Id
    private String idPerson;
    private String user;
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

    public Person(PersonInputDto inputDao) {
        this.idPerson = inputDao.getIdPerson();
        this.user = inputDao.getUser();
        this.password = inputDao.getPassword();
        this.name = inputDao.getName();
        this.surname = inputDao.getSurname();
        this.companyEmail = inputDao.getCompanyEmail();
        this.personalEmail = inputDao.getPersonalEmail();
        this.city = inputDao.getCity();
        this.active = inputDao.getActive();
        this.createdDate = inputDao.getCreatedDate();
        this.imageUrl = inputDao.getImageUrl();
        this.terminationDate = inputDao.getTerminationDate();
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
                this.terminationDate
        );
    }
}
