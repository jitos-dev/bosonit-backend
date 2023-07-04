package com.bosonit.formacion.block14springsecurity.asignatura.domain;

import com.bosonit.formacion.block14springsecurity.student.domain.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table
public class Asignatura {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_Asignatura", unique = true)
    private Integer idAsignatura;
    @Column(unique = true)
    private String nombreAsignatura;
    private String comments;
    private String branch;
    @Column(nullable = false)
    private Date initial_date = new Date();
    private Date finish_date;


    //------------------------Relaciones------------------------
    @ManyToMany(mappedBy = "asignaturas")//, cascade=CascadeType.ALL)
    private List<Student> students = new ArrayList<>();
    // ----------------------------------------------------------


}

