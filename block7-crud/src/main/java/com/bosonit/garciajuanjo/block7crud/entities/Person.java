package com.bosonit.garciajuanjo.block7crud.entities;

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
    private Long idPerson;
    private String name;

    private String age;

    private String population;
}
