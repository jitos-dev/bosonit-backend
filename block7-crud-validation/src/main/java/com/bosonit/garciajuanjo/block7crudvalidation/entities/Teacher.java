package com.bosonit.garciajuanjo.block7crudvalidation.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {

    @Id
    @GeneratedValue
    @Column(name = "id_teacher")
    @JsonProperty(value = "id_teacher")
    private Long idTeacher;

    private String comments;

    @Enumerated(EnumType.STRING)
    private Branch branch;
}
