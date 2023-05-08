package com.bosonit.garciajuanjo.block7crudvalidation.controllers;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentOutputDto;
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
    public StudentOutputDto getById(@PathVariable String id) {
        return service.getById(id).orElseThrow();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentOutputDto addStudent(@RequestBody StudentInputDto studentInputDto) {
        Optional<StudentOutputDto> optional = service.save(studentInputDto);

        return optional.orElseThrow();
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentOutputDto update(@RequestBody StudentInputDto studentInputDto, @PathVariable String id) {
        return service.update(id, studentInputDto).orElseThrow();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
