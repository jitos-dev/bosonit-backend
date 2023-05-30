package com.bosonit.garciajuanjo.Block13mongodb.controllers;

import com.bosonit.garciajuanjo.Block13mongodb.models.dtos.PersonInputDto;
import com.bosonit.garciajuanjo.Block13mongodb.models.dtos.PersonOutputDto;
import com.bosonit.garciajuanjo.Block13mongodb.services.PersonService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("person")
@AllArgsConstructor
public class PersonController {

    private PersonService personService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PersonOutputDto> findAll() {
        return personService.findAll();
    }

    @GetMapping("page/{numberPage}")
    @ResponseStatus(HttpStatus.OK)
    public List<PersonOutputDto> findAll(
            @PathVariable Integer numberPage,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
            return personService.findAll(numberPage, pageSize);
    }

    @GetMapping("/{personId}")
    @ResponseStatus(HttpStatus.OK)
    public PersonOutputDto findById(@PathVariable String personId) {
        return personService.findById(personId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonOutputDto add(@Valid @RequestBody PersonInputDto inputDto) {
        return personService.save(inputDto);
    }

    @PutMapping("{personId}")
    @ResponseStatus(HttpStatus.OK)
    public PersonOutputDto update(@Valid @RequestBody PersonInputDto inputDto, @PathVariable String personId) {
        inputDto.setIdPerson(personId);
        return personService.update(inputDto);
    }

    @DeleteMapping("{personId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String personId) {
        personService.delete(personId);
    }

}
