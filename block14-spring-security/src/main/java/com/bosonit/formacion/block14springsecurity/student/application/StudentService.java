package com.bosonit.formacion.block14springsecurity.student.application;

import com.bosonit.formacion.block14springsecurity.student.controller.dto.studentInputDto.StudentInputDto;
import com.bosonit.formacion.block14springsecurity.student.controller.dto.studentOutputDto.StudentOutputDtoSimple;

import java.util.List;

public interface StudentService {
    StudentOutputDtoSimple addStudent(StudentInputDto StudentInputDto);

    String assignAsignaturaToStudent(Integer idStudent, List<Integer> idsAsignaturas);

    String removeAsignaturasFromStudent(Integer idStudent, List<Integer> idsAsignaturas);

    StudentOutputDtoSimple getStudentById(Integer id, String outputType);

    List<StudentOutputDtoSimple> getAllStudents();

    StudentOutputDtoSimple updateStudent(Integer id, StudentInputDto studentInputDto);

    String deleteStudentById(Integer id);
}
