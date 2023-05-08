package com.bosonit.garciajuanjo.block7crudvalidation.entities;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentSubjectInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentSubjectOutputDto;
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
    private String idSubject;

    private String comments;

    @Column(name = "initial_date", nullable = false)
    private Date initialDate;

    @Column(name = "finish_date")
    private Date finishDate;

    @Enumerated(EnumType.STRING)
    private Subject subject;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public StudentSubject(StudentSubjectInputDto inputDto) {
        this.idSubject = inputDto.getIdSubject();
        this.comments = inputDto.getComments();
        this.initialDate = inputDto.getInitialDate();
        this.finishDate = inputDto.getFinishDate();
        this.subject = inputDto.getSubject();
        this.student = new Student(inputDto.getStudent());
    }

    public StudentSubjectOutputDto studentSubjectToStudentSubjectOutputDto() {
        return new StudentSubjectOutputDto(
                this.idSubject,
                this.subject,
                this.comments,
                this.initialDate,
                this.finishDate,
                this.student.studentToStudentOutputDto()
        );
    }
}
