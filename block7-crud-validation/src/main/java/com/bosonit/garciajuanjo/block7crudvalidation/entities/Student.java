package com.bosonit.garciajuanjo.block7crudvalidation.entities;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentOutputDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(generator = "myGenerator")
    @GenericGenerator(name = "myGenerator", strategy = "com.bosonit.garciajuanjo.block7crudvalidation.utils.MyIdentifierGenerator")
    @Column(name = "id_student")
    private String idStudent;

    @Column(name = "num_hours_week", nullable = false)
    private Integer numHoursWeek;

    private String comments;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Branch branch;

    @OneToOne
    @JoinColumn(name = "person_id", unique = true)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    //@JsonIgnoreProperties(value = {"students"})
    private Teacher teacher;

    public Student(StudentInputDto dto) {
        this.idStudent = dto.getIdStudent();
        this.numHoursWeek = dto.getNumHoursWeek();
        this.comments = dto.getComments();
        this.branch = dto.getBranch();
    }

    public StudentOutputDto studentToStudentOutputDto() {
        return new StudentOutputDto(
                this.idStudent,
                this.numHoursWeek,
                this.comments,
                this.branch,
                this.person.personToPersonOutputDto(),
                this.teacher.getIdTeacher()
        );
    }

    public StudentInputDto studentToStudentInputDto() {
        return new StudentInputDto(
                this.idStudent,
                this.numHoursWeek,
                this.comments,
                this.branch,
                this.person.getIdPerson(),
                this.teacher.getIdTeacher()
        );
    }
}

