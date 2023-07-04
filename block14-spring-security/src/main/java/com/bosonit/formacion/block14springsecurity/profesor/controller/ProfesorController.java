package com.bosonit.formacion.block14springsecurity.profesor.controller;

import com.bosonit.formacion.block14springsecurity.profesor.application.ProfesorService;
import com.bosonit.formacion.block14springsecurity.profesor.controller.dto.ProfesorInputDto;
import com.bosonit.formacion.block14springsecurity.profesor.controller.dto.ProfesorOutputDto;
import com.bosonit.formacion.block14springsecurity.student.application.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profesor")
public class ProfesorController {
    @Autowired
    ProfesorService profesorService;

    @Autowired
    StudentService studentService;

    @PostMapping
    public ResponseEntity<ProfesorOutputDto> addProfesor(@RequestBody ProfesorInputDto profesorInputDto) {
        return new ResponseEntity<>(profesorService.addProfesor(profesorInputDto), HttpStatus.CREATED);
    }

    @PostMapping("/assignStudentToProfesor")
    public ResponseEntity<String> assignStudentToProfesor(@RequestParam("idProfesor") Integer idProfesor,
                                                          @RequestParam("idStudent") Integer idStudent) {
        return new ResponseEntity<>(profesorService.assignStudentToProfesor(idProfesor, idStudent), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesorOutputDto> getProfesorById(@PathVariable Integer id) {
        return new ResponseEntity<>(profesorService.getProfesorById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllProfesores() {
        try {
            List<ProfesorOutputDto> profesorOutputDtoList = profesorService.getAllProfesores();
            if (profesorOutputDtoList.isEmpty()) {
                throw new Exception("Lista vacía.");
            }
            return new ResponseEntity<>(profesorService.getAllProfesores(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfesorOutputDto> updateProfesor(@PathVariable("id") Integer id, @RequestBody ProfesorInputDto profesorInputDto) {
        return new ResponseEntity<>(profesorService.updateProfesor(id, profesorInputDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProfesorById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(profesorService.deleteProfesorById(id), HttpStatus.OK);
    }
}



