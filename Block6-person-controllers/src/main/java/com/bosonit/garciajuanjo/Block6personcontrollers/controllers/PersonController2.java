package com.bosonit.garciajuanjo.Block6personcontrollers.controllers;

import com.bosonit.garciajuanjo.Block6personcontrollers.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/controlador2")
public class PersonController2 {

    @Autowired
    private PersonController1 controller1;

    @GetMapping(value = "/getPersona")
    public ResponseEntity<Person> getPersona() {
        Person person = controller1.addPersona("Juan Jose", "Sabiote", "37").getBody();

        if (person != null)
            person.setAge(person.getAge() * 2);

        return ResponseEntity.ok(person);
    }
}
