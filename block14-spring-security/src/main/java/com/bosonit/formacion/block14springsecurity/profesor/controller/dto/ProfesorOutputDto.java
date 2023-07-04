package com.bosonit.formacion.block14springsecurity.profesor.controller.dto;
import com.bosonit.formacion.block14springsecurity.profesor.domain.Profesor;
import com.bosonit.formacion.block14springsecurity.student.controller.dto.studentOutputDto.StudentOutputDtoSimple;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfesorOutputDto {
    private Integer idProfesor;
    private String comments;
    private String branch;
    private List<StudentOutputDtoSimple> listStudents = new ArrayList<>();

    //Constructor con Profesor por parámetro.
    public ProfesorOutputDto(Profesor profesor) {
        this.idProfesor = profesor.getIdProfesor();
        this.comments = profesor.getComments();
        this.branch = profesor.getBranch();
        this.listStudents = profesor.getStudents().stream().map(StudentOutputDtoSimple::new).toList();
    }
}

