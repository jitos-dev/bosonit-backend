package com.bosonit.garciajuanjo.block7crud.controllers;

import com.bosonit.garciajuanjo.block7crud.entities.Person;
import com.bosonit.garciajuanjo.block7crud.services.PersonService;
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
    public ResponseEntity<List<Person>> allPersons() {
        return ResponseEntity.ok().body(service.getAll());
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<Person> getById(@PathVariable Long id) {
        Optional<Person> person = service.getPersonById(id);

        return person.map(value ->
                        ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping(value = "/name/{name}")
    public ResponseEntity<List<Person>> personByName(@PathVariable String name) {
        List<Person> persons = service.getPersonsByName(name);

        if (persons.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().body(persons);
    }

    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(person).orElseThrow());
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Person> update(@RequestBody Person person, @PathVariable Long id) {
        Optional<Person> optPerson = service.update(id, person);

        return optPerson.map(value ->
                        ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Person> person = service.delete(id);

        if (person.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }

}
