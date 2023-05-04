package com.bosonit.garciajuanjo.block7crudvalidation.controllers;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService service;

    @GetMapping
    public ResponseEntity<List<PersonOutputDto>> allPersons() {
        return ResponseEntity.ok().body(service.getAll());
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<PersonOutputDto> getById(@PathVariable Integer id) {
        Optional<PersonOutputDto> person = service.getPersonById(id);

        return person.map(value ->
                        ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping(value = "/name/{name}")
    public ResponseEntity<List<PersonOutputDto>> personByName(@PathVariable String name) {
        List<PersonOutputDto> persons = service.getPersonsByName(name);

        if (persons.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().body(persons);
    }

    @PostMapping
    public ResponseEntity<PersonOutputDto> addPerson(@RequestBody PersonInputDto personInputDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(personInputDto).orElseThrow());
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PersonOutputDto> update(@RequestBody PersonInputDto personInputDto, @PathVariable Integer id) {
        Optional<PersonOutputDto> optPerson = service.update(id, personInputDto);

        return optPerson.map(value ->
                        ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Optional<PersonOutputDto> person = service.delete(id);

        if (person.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }

}
