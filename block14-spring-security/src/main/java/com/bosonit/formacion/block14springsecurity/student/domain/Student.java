package com.bosonit.formacion.block14springsecurity.student.domain;

import com.bosonit.formacion.block14springsecurity.asignatura.domain.Asignatura;
import com.bosonit.formacion.block14springsecurity.persona.domain.Persona;
import com.bosonit.formacion.block14springsecurity.profesor.domain.Profesor;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Table
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_Student", unique = true)
    private Integer idStudent;

    @Column(nullable = true)
    private Integer numHourWeek;

    private String comments;

    @Column(nullable = true)
    private String branch;

    //------------------------Relaciones------------------------
    @OneToOne//(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "id_persona", nullable = false, unique = true)
    private Persona persona;

    @ManyToOne//(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    // Un estudiante solo puede tener un profesor.(Luego muchos estudiantes tendrán a un solo profesor)
    @JoinColumn(name = "id_profesor")
    private Profesor profesor;

    @ManyToMany//(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Asignatura> asignaturas = new ArrayList<>();

    // ----------------------------------------------------------

}
