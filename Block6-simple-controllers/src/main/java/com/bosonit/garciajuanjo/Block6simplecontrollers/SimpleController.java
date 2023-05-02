package com.bosonit.garciajuanjo.Block6simplecontrollers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class SimpleController {

    @GetMapping(value = "/user/{nombre}")
    public String getMessage(@PathVariable String nombre) {
        return "Hola " + nombre;
    }
}
