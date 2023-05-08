package com.bosonit.garciajuanjo.block7crudvalidation.entities;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.TeacherInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.TeacherOutputDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

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
    @JsonProperty(value = "id_teacher")
    private String idTeacher;

    private String comments;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Branch branch;

    @OneToOne
    @JoinColumn(name = "id_person")
    private Person person;

    public Teacher(TeacherInputDto dto) {
        this.idTeacher = dto.getIdTeacher();
        this.comments = dto.getComments();
        this.branch = dto.getBranch();
        this.person = new Person(dto.getPerson());
    }

    public TeacherOutputDto teacherToTeacherOutputDto(){
        return new TeacherOutputDto(
                this.idTeacher,
                this.comments,
                this.branch,
                this.person.personToPersonOutputDto()
        );
    }
}
