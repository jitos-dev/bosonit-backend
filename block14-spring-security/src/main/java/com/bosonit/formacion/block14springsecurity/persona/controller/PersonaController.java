package com.bosonit.formacion.block14springsecurity.persona.controller;

import com.bosonit.formacion.block14springsecurity.persona.application.PersonaService;
import com.bosonit.formacion.block14springsecurity.persona.controller.dto.PersonaInputDto;
import com.bosonit.formacion.block14springsecurity.persona.controller.dto.PersonaOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/persona")
public class PersonaController {
    @Autowired
    PersonaService personaService;


    @PostMapping
    public ResponseEntity<PersonaOutputDto> addPersona(@RequestBody PersonaInputDto personaInputDto) {
        return new ResponseEntity<>(personaService.addPersona(personaInputDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonaOutputDto> getPersonasById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(personaService.getPersonaById(id), HttpStatus.OK);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> getPersonasByName(@PathVariable("nombre") String nombre) {
        try {
            List<PersonaOutputDto> personaOutputDtoList = personaService.getPersonasByName(nombre);
            if (personaOutputDtoList.isEmpty()) {
                throw new Exception("Persona no encontrada.");
            }
            return new ResponseEntity<>(personaOutputDtoList, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listaPersonas() {
        try {
            List<PersonaOutputDto> personaOutputDtoList = personaService.getAllPersonas();
            if (personaOutputDtoList.isEmpty()) {
                throw new Exception("Lista vacía.");
            }
            return new ResponseEntity<>(personaOutputDtoList, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonaOutputDto> updatePersona(@PathVariable("id") Integer id, @RequestBody PersonaInputDto personaInputDto) {
        return new ResponseEntity<>(personaService.updatePersona(id, personaInputDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borraPersonaById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(personaService.deletePersona(id), HttpStatus.OK);
    }
}