package com.bosonit.garciajuanjo.block7crud.controllers;

import com.bosonit.garciajuanjo.block7crud.entities.Person;
import com.bosonit.garciajuanjo.block7crud.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService service;

    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        Optional<Person> optPerson = service.save(person);

        return optPerson.map(value ->
                        ResponseEntity.status(HttpStatus.CREATED).body(value))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Person> getById(@PathVariable Long id) {
        Optional<Person> person = service.getPersonById(id);

        return person.map(value ->
                        ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

}
