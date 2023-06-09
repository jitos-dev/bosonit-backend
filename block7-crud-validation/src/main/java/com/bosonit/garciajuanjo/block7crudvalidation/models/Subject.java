package com.bosonit.garciajuanjo.block7crudvalidation.models;

import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.StudentOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.SubjectInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.SubjectOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.SubjectSimpleOutputDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Subject {

    @Id
    @GeneratedValue(generator = "myGenerator")
    @GenericGenerator(name = "myGenerator", strategy = "com.bosonit.garciajuanjo.block7crudvalidation.utils.MyIdentifierGenerator")
    @Column(name = "id_subject")
    @EqualsAndHashCode.Include
    private String idSubject;

    private String comments;

    @Column(name = "initial_date", nullable = false)
    private Date initialDate;

    @Column(name = "finish_date")
    private Date finishDate;

    @Enumerated(EnumType.STRING)
    @EqualsAndHashCode.Include
    @Column(name = "subject_name", nullable = false)
    private SubjectName subjectName;

    @ManyToMany(mappedBy = "subjects")
    private Set<Student> students = new HashSet<>();

    public Subject(SubjectInputDto inputDto) {
        this.idSubject = inputDto.getSubjectId();
        this.comments = inputDto.getComments();
        this.initialDate = inputDto.getInitialDate();
        this.finishDate = inputDto.getFinishDate();
        this.subjectName = SubjectName.valueOf(inputDto.getSubjectName());
    }

    public SubjectOutputDto subjectToSubjectOutputDto() {
        List<StudentOutputDto> studentsDto = this.students
                .stream()
                .map(Student::studentToStudentOutputDto)
                .toList();

        return new SubjectOutputDto(
                this.idSubject,
                this.subjectName,
                this.comments,
                this.initialDate,
                this.finishDate,
                studentsDto
        );
    }

    public SubjectSimpleOutputDto subjectToSubjectSimpleOutputDto() {
        return new SubjectSimpleOutputDto(
                this.idSubject,
                this.subjectName,
                this.comments,
                this.initialDate,
                this.finishDate
        );
    }
}
