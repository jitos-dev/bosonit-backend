package com.bosonit.garciajuanjo.block7crudvalidation.models;

import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.StudentSimpleOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.TeacherInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.TeacherOutputDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
