package com.bosonit.garciajuanjo.Block6personcontrollers.controllers;

import com.bosonit.garciajuanjo.Block6personcontrollers.Person;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/controlador1")
@AllArgsConstructor
public class PersonController1 {

    private PersonService service;

    @GetMapping(value = "/addPersona")
    public ResponseEntity<Person> addPersona(
            @RequestHeader("name") String name,
            @RequestHeader("population") String population,
            @RequestHeader("age") String age) {

        return ResponseEntity.status(HttpStatus.OK).body(service.getPerson(name, population, age));
    }
}
