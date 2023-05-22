package com.bosonit.garciajuanjo.block7crudvalidation.controllers;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.Person;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonCodepenInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonCompleteOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.services.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class PersonCorsController {

    private PersonService personService;

    /*Este metodo es para realizar las pruebas del ejercicio del bloque 11 el apartado de cors. La prueba hay que
     * hacerla desde la pagina: https://codepen.io/de4imo/pen/VwMRENP
     * En el cors especifico que url es la que puede hacer peticiones a esta funcion que para este caso e la que
     * esta en @CrossOrigin*/
    @CrossOrigin(origins = "https://cdpn.io")
    @PostMapping("addperson")
    @ResponseStatus(HttpStatus.CREATED)
    public PersonOutputDto addPerson(@RequestBody PersonCodepenInputDto codepenInputDto) {
        return personService.save(new Person(codepenInputDto).personToPersonInputDto()).orElseThrow();
    }


    @CrossOrigin(origins = "https://cdpn.io")
    @GetMapping("getall")
    @ResponseStatus(HttpStatus.OK)
    public List<PersonCompleteOutputDto> getAllPersons() {
        return personService.getAll();
    }
}
