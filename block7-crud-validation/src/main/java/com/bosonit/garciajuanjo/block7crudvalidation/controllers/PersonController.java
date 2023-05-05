package com.bosonit.garciajuanjo.block7crudvalidation.controllers;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PersonOutputDto> allPersons() {
        return service.getAll();
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonOutputDto getById(@PathVariable Integer id) {
        return service.getPersonById(id).orElseThrow();
    }

    @GetMapping(value = "/user/{user}")
    @ResponseStatus(HttpStatus.OK)
    public List<PersonOutputDto> personByUser(@PathVariable String user) {
        return service.getPersonsByUser(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonOutputDto addPerson(@RequestBody PersonInputDto personInputDto) {
        return service.save(personInputDto).orElseThrow();
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonOutputDto update(@RequestBody PersonInputDto personInputDto, @PathVariable Integer id) {
        return service.update(id, personInputDto).orElseThrow();
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
