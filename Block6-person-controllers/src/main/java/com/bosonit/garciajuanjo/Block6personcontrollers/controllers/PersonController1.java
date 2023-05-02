package com.bosonit.garciajuanjo.Block6personcontrollers.controllers;

import com.bosonit.garciajuanjo.Block6personcontrollers.services.CityService;
import com.bosonit.garciajuanjo.Block6personcontrollers.services.PersonService;
import com.bosonit.garciajuanjo.Block6personcontrollers.entities.City;
import com.bosonit.garciajuanjo.Block6personcontrollers.entities.Person;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/controlador1")
@AllArgsConstructor
public class PersonController1 {

    private final PersonService service;

    private final CityService cityService;

//    public PersonController1(PersonService service, CityService cityService) {
//        this.service = service;
//        this.cityService = cityService;
//    }

    @GetMapping(value = "/addPersona")
    public ResponseEntity<Person> addPersona(
            @RequestHeader("name") String name,
            @RequestHeader("population") String population,
            @RequestHeader("age") String age) {

        return ResponseEntity.status(HttpStatus.OK).body(service.getPerson(name, population, age));
    }

    @PostMapping(value = "/addCiudad")
    public ResponseEntity<?> addCity(@RequestBody City city) {
        this.cityService.getCities().add(city);

        return ResponseEntity.ok(this.cityService.getCities());
    }
}
