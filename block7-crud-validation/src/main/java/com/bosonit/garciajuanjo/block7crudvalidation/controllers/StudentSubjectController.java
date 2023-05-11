package com.bosonit.garciajuanjo.block7crudvalidation.controllers;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.SubjectInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.SubjectListOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.SubjectOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("student-subject")
public class StudentSubjectController {

    @Autowired
    private SubjectService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SubjectOutputDto> getAll() {
        return service.findAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public SubjectOutputDto getById(@PathVariable String id) {
        return service.findById(id).orElseThrow();
    }

    @GetMapping("student/{id}")
    public SubjectListOutputDto getByUserId(@PathVariable String userId) {
        return service.findByStudentId(userId).orElseThrow();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubjectOutputDto addStudentSubject(@RequestBody SubjectInputDto subjectInputDto) {
        Optional<SubjectOutputDto> optional = service.save(subjectInputDto);

        return optional.orElseThrow();
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public SubjectOutputDto update(@RequestBody SubjectInputDto subjectInputDto, @PathVariable String id) {
        return service.update(id, subjectInputDto).orElseThrow();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
