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
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(person).orElseThrow());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Person> getById(@PathVariable Long id) {
        Optional<Person> person = service.getPersonById(id);

        return person.map(value ->
                        ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Person> update(@RequestBody Person person, @PathVariable Long id) {
        if (!service.existById(id))
            return ResponseEntity.notFound().build();

        Person personUpdated = new Person();
        personUpdated.setIdPerson(id);
        personUpdated.setName(person.getName() == null ? "" : person.getName());
        personUpdated.setAge(person.getAge() == null ? "" : person.getAge());
        personUpdated.setPopulation(person.getPopulation() == null ? "" : person.getPopulation());

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(personUpdated).orElseThrow());
    }

}
