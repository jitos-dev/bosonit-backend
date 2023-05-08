package com.bosonit.garciajuanjo.block7crudvalidation.controllers;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("student")
public class StudentController {

    @Autowired
    private StudentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentOutputDto addStudent(@RequestBody StudentInputDto studentInputDto) {
        Optional<StudentOutputDto> optional = service.save(studentInputDto);

        return optional.orElseThrow();
    }
}
