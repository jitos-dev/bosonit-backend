package com.bosonit.garciajuanjo.block7crudvalidation.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Entity
@Table(name = "student_subject")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentSubject {

    @Id
    @GeneratedValue(generator = "myGenerator")
    @GenericGenerator(name = "myGenerator", strategy = "com.bosonit.garciajuanjo.block7crudvalidation.utils.MyIdentifierGenerator")
    @Column(name = "id_subject")
    @JsonProperty(value = "id_subject")
    private Long idSubject;

    private String subject;

    private String comments;

    @Column(name = "initial_date", nullable = false)
    @JsonProperty(value = "initial_date")
    private Date initialDate;

    @Column(name = "finish_date")
    @JsonProperty(value = "finish_date")
    private Date finishDate;
}
