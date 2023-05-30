package com.bosonit.garciajuanjo.block7crudvalidation.controllers;

import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.*;
import com.bosonit.garciajuanjo.block7crudvalidation.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("student")
public class StudentController {

    @Autowired
    private StudentService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<StudentOutputDto> getAll() {
        return service.findAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonCompleteOutputDto getById(
            @PathVariable String id,
            @RequestParam(required = false, defaultValue = "simple") String outputType) {

        return service.getById(id, outputType).orElseThrow();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentOutputDto addStudent(@RequestBody StudentInputDto studentInputDto) {
        Optional<StudentOutputDto> optional = service.save(studentInputDto);

        return optional.orElseThrow();
    }

    @PutMapping("subject/{studentId}")
    @ResponseStatus(HttpStatus.OK)
    public StudentOutputDto addSubject(
            @RequestBody List<String> subjectsIds,
            @PathVariable String studentId) {
        return service.addSubjects(subjectsIds, studentId).orElseThrow();
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentOutputDto update(@RequestBody StudentInputDto studentInputDto, @PathVariable String id) {
        return service.update(id, studentInputDto).orElseThrow();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @DeleteMapping("subject/{studentId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteByIds(
            @PathVariable String studentId,
            @RequestBody List<String> subjectIds) {
        service.deleteSubjects(subjectIds, studentId);
    }
}
