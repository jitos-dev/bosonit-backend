package com.bosonit.garciajuanjo.Block6simplecontrollers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
public class SimpleController {

    @GetMapping(value = "/user/{nombre}")
    public String getMessage(@PathVariable String nombre) {
        return "Hola " + nombre;
    }

    @PostMapping(value = "/useradd")
    public ResponseEntity<Persona> addPerson(@RequestBody Persona person) {
        person.setEdad(person.getEdad() + 1);

        return ResponseEntity.ok(person);
    }
}
