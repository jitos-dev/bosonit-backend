package com.bosonit.garciajuanjo.Block6personcontrollers.controllers;

import com.bosonit.garciajuanjo.Block6personcontrollers.configurations.PersonConfiguration;
import com.bosonit.garciajuanjo.Block6personcontrollers.entities.Person;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "controlador")
public class PointThreeController {

    @Autowired
    @Qualifier(value = "configuration")
    private PersonConfiguration personConfiguration;


    @GetMapping(value = "/bean/{bean}")
    public ResponseEntity<?> getBeanPerson(@PathVariable String bean) {

        return switch (bean){
            case "bean1" ->
                ResponseEntity.ok(personConfiguration.bean1());

            case "bean2" ->
                    ResponseEntity.ok(personConfiguration.bean2());

            case "bean3" ->
                    ResponseEntity.ok(personConfiguration.bean3());

            default ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(bean);

        };
    }
}
