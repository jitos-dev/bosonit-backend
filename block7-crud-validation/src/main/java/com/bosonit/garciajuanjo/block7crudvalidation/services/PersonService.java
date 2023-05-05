package com.bosonit.garciajuanjo.block7crudvalidation.services;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonOutputDto;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    Optional<PersonOutputDto> save(PersonInputDto personInputDto);

    Optional<PersonOutputDto> getPersonById(String id);

    List<PersonOutputDto> getPersonsByUser(String name);

    List<PersonOutputDto> getAll();

    void delete(String id);

    Optional<PersonOutputDto> update(String id, PersonInputDto personInputDto);
}
