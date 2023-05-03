package com.bosonit.garciajuanjo.block7crud.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "person")
@Getter
@Setter
public class Person {

    @Id
    @GeneratedValue
    @Column(name = "id_person")
    @JsonProperty(value = "id")
    private Long idPerson;
    private String name;

    private String age;

    private String population;
}
