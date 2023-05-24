package com.bosonit.garciajuanjo.block7crudvalidation.controllers;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonCompleteOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.TeacherOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PersonCompleteOutputDto> allPersons(@RequestParam(required = false, defaultValue = "simple") String outputType) {
        return service.getAll(outputType);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonCompleteOutputDto getById(
            @PathVariable String id,
            @RequestParam(required = false, defaultValue = "simple") String outputType) {
        return service.getById(id, outputType).orElseThrow();
    }

    @GetMapping(value = "/user/{user}")
    @ResponseStatus(HttpStatus.OK)
    public List<PersonCompleteOutputDto> personByUser(
            @PathVariable String user,
            @RequestParam(required = false, defaultValue = "simple") String outputType) {
        return service.getByUser(user, outputType);
    }

    @GetMapping("teacher/{teacherId}")
    @ResponseStatus(HttpStatus.OK)
    public TeacherOutputDto getTeacherById(@PathVariable String teacherId) {
        return service.getTeacherByIdTeacher(teacherId).orElseThrow();
    }

    @GetMapping("findBy")
    @ResponseStatus(HttpStatus.OK)
    public List<PersonOutputDto> findPersonsBy(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "user", required = false) String user,
            @RequestParam(value = "surname", required = false) String surname,
            @RequestParam(value = "created_date", required = false) Date createdDate,
            @RequestParam(value = "order_created_date_by", required = false, defaultValue = "asc") String orderDate,
            @RequestParam(value = "order_by_user", required = false) Boolean orderByUser,
            @RequestParam(value = "order_by_name", required = false) Boolean orderByName
    ) {

        
        return null;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonOutputDto addPerson(@RequestBody PersonInputDto personInputDto) {
        return service.save(personInputDto).orElseThrow();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonOutputDto update(@RequestBody PersonInputDto personInputDto, @PathVariable String id) {
        return service.update(id, personInputDto).orElseThrow();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
