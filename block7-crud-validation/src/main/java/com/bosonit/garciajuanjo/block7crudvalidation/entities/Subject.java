package com.bosonit.garciajuanjo.block7crudvalidation.entities;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentSubjectInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentSubjectOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentSubjectSimpleOutputDto;
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
public class Subject {

    @Id
    @GeneratedValue(generator = "myGenerator")
    @GenericGenerator(name = "myGenerator", strategy = "com.bosonit.garciajuanjo.block7crudvalidation.utils.MyIdentifierGenerator")
    @Column(name = "id_subject")
    private String idStudentSubject;

    private String comments;

    @Column(name = "initial_date", nullable = false)
    private Date initialDate;

    @Column(name = "finish_date")
    private Date finishDate;

    @Enumerated(EnumType.STRING)
    private SubjectName subjectName;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public Subject(StudentSubjectInputDto inputDto) {
        this.idStudentSubject = inputDto.getIdStudentSubject();
        this.comments = inputDto.getComments();
        this.initialDate = inputDto.getInitialDate();
        this.finishDate = inputDto.getFinishDate();
        this.subjectName = inputDto.getSubjectName();
    }

    public StudentSubjectOutputDto studentSubjectToStudentSubjectOutputDto() {
        return new StudentSubjectOutputDto(
                this.idStudentSubject,
                this.subjectName,
                this.comments,
                this.initialDate,
                this.finishDate,
                this.student.studentToStudentOutputDto()
        );
    }

    public StudentSubjectSimpleOutputDto studentSubjectToStudentSubjectSimpleOutputDto() {
        return new StudentSubjectSimpleOutputDto(
                this.idStudentSubject,
                this.subjectName,
                this.comments,
                this.initialDate,
                this.finishDate
        );
    }

    public StudentSubjectInputDto studentSubjectToStudentSubjectInputDto() {
        return new StudentSubjectInputDto(
                this.idStudentSubject,
                this.subjectName,
                this.comments,
                this.initialDate,
                this.finishDate,
                this.student.getIdStudent()
        );
    }
}
