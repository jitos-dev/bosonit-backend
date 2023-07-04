package com.bosonit.formacion.block14springsecurity.student.controller;

import com.bosonit.formacion.block14springsecurity.student.application.StudentService;
import com.bosonit.formacion.block14springsecurity.student.controller.dto.studentInputDto.StudentInputDto;
import com.bosonit.formacion.block14springsecurity.student.controller.dto.studentOutputDto.StudentOutputDtoSimple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentOutputDtoSimple> addStudent(@RequestBody StudentInputDto studentInputDto) {
        return new ResponseEntity<>(studentService.addStudent(studentInputDto), HttpStatus.CREATED);
    }

    @PostMapping("/assignAsignaturaToStudent")
    public ResponseEntity<String> addStudentToAsignatura(@RequestParam("idStudent") Integer idStudent,
                                                         @RequestParam("idsAsignaturas") List<Integer> idsAsignaturas) {
        return new ResponseEntity<>(studentService.assignAsignaturaToStudent(idStudent, idsAsignaturas), HttpStatus.OK);
    }

    @PutMapping("/removeAsignaturasFromStudent")
    public ResponseEntity<String> removeAsignaturasFromStudent(@RequestParam("idStudent") Integer idStudent,
                                                               @RequestParam("idsAsignaturas") List<Integer> idsAsignaturas) {
        return new ResponseEntity<>(studentService.removeAsignaturasFromStudent(idStudent, idsAsignaturas), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentOutputDtoSimple> getStudentById(@PathVariable Integer id,
                                                                 @RequestParam(value = "outputType", defaultValue = "simple") String outputType) {
        return new ResponseEntity<>(studentService.getStudentById(id, outputType), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllStudents() {
        try {
            List<StudentOutputDtoSimple> studentOutputDtoSimpleList = studentService.getAllStudents();
            if (studentOutputDtoSimpleList.isEmpty()) {
                throw new Exception("Lista vacía.");
            }
            return new ResponseEntity<>(studentOutputDtoSimpleList, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentOutputDtoSimple> updateStudent(@PathVariable("id") Integer id, @RequestBody StudentInputDto studentInputDto) {
        return new ResponseEntity<>(studentService.updateStudent(id, studentInputDto), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudentById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(studentService.deleteStudentById(id), HttpStatus.OK);
    }
}
