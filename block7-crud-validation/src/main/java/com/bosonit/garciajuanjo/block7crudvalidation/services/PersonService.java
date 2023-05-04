package com.bosonit.garciajuanjo.block7crudvalidation.services;


import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonOutputDto;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    Optional<PersonOutputDto> save(PersonInputDto personInputDto) throws Exception;

    Optional<PersonOutputDto> getPersonById(int id);

    List<PersonOutputDto> getPersonsByName(String name);

    List<PersonOutputDto> getAll();

    Optional<PersonOutputDto> delete(int id);

    Optional<PersonOutputDto> update(int id, PersonInputDto personInputDto) throws Exception;
}
