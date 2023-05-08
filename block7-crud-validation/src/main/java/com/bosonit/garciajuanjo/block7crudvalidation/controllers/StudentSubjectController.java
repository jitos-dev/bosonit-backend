package com.bosonit.garciajuanjo.block7crudvalidation.controllers;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentSubjectInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentSubjectOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.services.StudentSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("student-subject")
public class StudentSubjectController {

    @Autowired
    private StudentSubjectService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<StudentSubjectOutputDto> getAll() {
        return service.findAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentSubjectOutputDto getById(@PathVariable String id) {
        return service.findById(id).orElseThrow();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentSubjectOutputDto addStudentSubject(@RequestBody StudentSubjectInputDto studentSubjectInputDto) {
        Optional<StudentSubjectOutputDto> optional = service.save(studentSubjectInputDto);

        return optional.orElseThrow();
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentSubjectOutputDto update(@RequestBody StudentSubjectInputDto studentSubjectInputDto, @PathVariable String id) {
        return service.update(id, studentSubjectInputDto).orElseThrow();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
