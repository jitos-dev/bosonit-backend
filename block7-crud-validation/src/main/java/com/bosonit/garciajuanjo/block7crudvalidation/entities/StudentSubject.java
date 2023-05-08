package com.bosonit.garciajuanjo.block7crudvalidation.entities;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentSubjectInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentSubjectOutputDto;
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
    private String idStudentSubject;

    private String comments;

    @Column(name = "initial_date", nullable = false)
    private Date initialDate;

    @Column(name = "finish_date")
    private Date finishDate;

    @Enumerated(EnumType.STRING)
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public StudentSubject(StudentSubjectInputDto inputDto) {
        this.idStudentSubject = inputDto.getIdStudentSubject();
        this.comments = inputDto.getComments();
        this.initialDate = inputDto.getInitialDate();
        this.finishDate = inputDto.getFinishDate();
        this.subject = inputDto.getSubject();
        this.student = new Student(inputDto.getStudent());
    }

    public StudentSubjectOutputDto studentSubjectToStudentSubjectOutputDto() {
        return new StudentSubjectOutputDto(
                this.idStudentSubject,
                this.subject,
                this.comments,
                this.initialDate,
                this.finishDate,
                this.student.studentToStudentOutputDto()
        );
    }

    public StudentSubjectInputDto studentSubjectToStudentSubjectInputDto() {
        return new StudentSubjectInputDto(
                this.idStudentSubject,
                this.subject,
                this.comments,
                this.initialDate,
                this.finishDate,
                this.student.studentToStudentInputDto()
        );
    }
}