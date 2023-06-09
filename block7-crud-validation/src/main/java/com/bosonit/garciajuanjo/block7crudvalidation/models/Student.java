package com.bosonit.garciajuanjo.block7crudvalidation.models;

import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.StudentInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.StudentOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.StudentSimpleOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.SubjectSimpleOutputDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@ToString
public class Student {

    @Id
    @GeneratedValue(generator = "myGenerator")
    @GenericGenerator(name = "myGenerator", strategy = "com.bosonit.garciajuanjo.block7crudvalidation.utils.MyIdentifierGenerator")
    @Column(name = "id_student")
    @EqualsAndHashCode.Include
    private String idStudent;

    @Column(name = "num_hours_week", nullable = false)
    private Integer numHoursWeek;

    private String comments;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Branch branch;

    @OneToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToMany
    @JoinTable(
            name = "student_subject",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<Subject> subjects = new HashSet<>();

    public Student(StudentInputDto dto) {
        this.numHoursWeek = dto.getNumHoursWeek();
        this.comments = dto.getComments();
        this.branch = dto.getBranch();
    }

    public StudentOutputDto studentToStudentOutputDto() {
        List<SubjectSimpleOutputDto> subjectsList = this.subjects.stream()
                .map(Subject::subjectToSubjectSimpleOutputDto)
                .toList();

        return new StudentOutputDto(
                this.idStudent,
                this.numHoursWeek,
                this.comments,
                this.branch,
                this.person.personToPersonOutputDto(),
                this.teacher.teacherToTeacherOutputDto(),
                subjectsList
        );
    }

    public StudentSimpleOutputDto studentToStudentSimpleOutputDto() {
        return new StudentSimpleOutputDto(
                this.idStudent,
                this.numHoursWeek,
                this.comments,
                this.branch,
                this.person.personToPersonOutputDto()
        );
    }
}

