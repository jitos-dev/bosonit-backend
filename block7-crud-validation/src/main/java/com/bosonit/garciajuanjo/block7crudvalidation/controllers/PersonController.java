package com.bosonit.garciajuanjo.block7crudvalidation.controllers;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonCompleteOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.TeacherOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.services.PersonService;
import jakarta.persistence.TemporalType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bosonit.garciajuanjo.block7crudvalidation.utils.Constants.*;

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

    @GetMapping("findBy/{numberPage}")
    @ResponseStatus(HttpStatus.OK)
    public List<PersonOutputDto> findPersonsBy(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String user,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) @DateTimeFormat(pattern = FORMAT_DATE) Date createdDate,
            @RequestParam(required = false, defaultValue = GREATER) String greaterOrLess,
            @RequestParam(required = false) Boolean orderByUser,
            @RequestParam(required = false) Boolean orderByName,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @PathVariable Integer numberPage
    ) {

        Map<String, Object> values = new HashMap<>();

        if (name != null) values.put(NAME, name);
        if (user != null) values.put(USER, user);
        if (surname != null) values.put(SURNAME, surname);
        if (createdDate != null) {
            values.put(CREATED_DATE, createdDate);
            values.put(GREATER_OR_LESS, greaterOrLess);
        }
        if (orderByUser != null) values.put(ORDER_BY_USER, orderByUser);
        if (orderByName != null) values.put(ORDER_BY_NAME, orderByName);

        values.put(PAGE_SIZE, pageSize);
        values.put(NUMBER_PAGE, numberPage);

        return service.getBy(values);
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
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
