package com.bosonit.garciajuanjo.Block13mongodb.controllers;

import com.bosonit.garciajuanjo.Block13mongodb.models.daos.PersonInputDto;
import com.bosonit.garciajuanjo.Block13mongodb.models.daos.PersonOutputDto;
import com.bosonit.garciajuanjo.Block13mongodb.services.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("person")
@AllArgsConstructor
public class PersonController {

    private PersonService personService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PersonOutputDto> findAll() {
        return personService.findAll();
    }

    @GetMapping("/{personId}")
    @ResponseStatus(HttpStatus.OK)
    public PersonOutputDto findById(@PathVariable String personId) {
        return personService.findById(personId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonOutputDto add(@RequestBody PersonInputDto inputDto) {
        return personService.save(inputDto);
    }
}
