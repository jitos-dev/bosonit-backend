package com.bosonit.garciajuanjo.block7crudvalidation.controllers;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.TeacherInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.TeacherOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("teacher")
public class TeacherController {

    @Autowired
    private TeacherService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TeacherOutputDto> getAll() {
        return service.findAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public TeacherOutputDto getById(@PathVariable String id) {
        return service.findById(id).orElseThrow();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeacherOutputDto addTeacher(@RequestBody TeacherInputDto teacherInputDto) {
        Optional<TeacherOutputDto> optional = service.save(teacherInputDto);

        return optional.orElseThrow();
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public TeacherOutputDto update(@RequestBody TeacherInputDto teacherInputDto, @PathVariable String id) {
        return service.update(id, teacherInputDto).orElseThrow();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
