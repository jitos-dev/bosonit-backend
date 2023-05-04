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

    @GetMapping(value = "/user/{user}")
    public ResponseEntity<List<PersonOutputDto>> personByName(@PathVariable String user) {
        List<PersonOutputDto> persons = service.getPersonsByUser(user);

        if (persons.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().body(persons);
    }

    @PostMapping
    public ResponseEntity<?> addPerson(@RequestBody PersonInputDto personInputDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(personInputDto).orElseThrow());

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@RequestBody PersonInputDto personInputDto, @PathVariable Integer id) {

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
