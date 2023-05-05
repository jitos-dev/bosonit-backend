package com.bosonit.garciajuanjo.block7crudvalidation.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue
    @Column(name = "id_student")
    @JsonProperty(value = "id_student")
    private Long idStudent;

    @Column(name = "num_hours_week")
    @JsonProperty(value = "num_hours_week")
    private Integer numHoursWeek;

    private String comments;

    @Enumerated(EnumType.STRING)
    private Branch branch;

}

