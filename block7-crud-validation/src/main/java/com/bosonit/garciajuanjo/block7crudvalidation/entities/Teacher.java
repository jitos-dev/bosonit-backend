package com.bosonit.garciajuanjo.block7crudvalidation.entities;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentSimpleOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.TeacherInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.TeacherOutputDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {

    @Id
    @GeneratedValue(generator = "myGenerator")
    @GenericGenerator(name = "myGenerator", strategy = "com.bosonit.garciajuanjo.block7crudvalidation.utils.MyIdentifierGenerator")
    @Column(name = "id_teacher")
    private String idTeacher;

    private String comments;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Branch branch;

    @OneToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    List<Student> students;

    public Teacher(TeacherInputDto dto) {
        this.idTeacher = dto.getIdTeacher();
        this.comments = dto.getComments();
        this.branch = dto.getBranch();
        this.students = new ArrayList<>();
    }

    public TeacherOutputDto teacherToTeacherOutputDto() {
        List<StudentSimpleOutputDto> studentsList = this.students
                .stream()
                .map(Student::studentToStudentSimpleOutputDto)
                .toList();

        return new TeacherOutputDto(
                this.idTeacher,
                this.comments,
                this.branch,
                this.person.personToPersonOutputDto(),
                studentsList
        );
    }

    public TeacherInputDto teacherToTeacherInputDto() {
        return new TeacherInputDto(
                this.idTeacher,
                this.comments,
                this.branch,
                this.person.getIdPerson()
        );
    }
}
