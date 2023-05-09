package com.bosonit.garciajuanjo.block7crudvalidation.services;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonStudentOutputDto;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    Optional<PersonOutputDto> save(PersonInputDto personInputDto);

    Optional<PersonStudentOutputDto> getById(String id);

    List<PersonOutputDto> getByUser(String name);

    List<PersonOutputDto> getAll();

    void delete(String id);

    Optional<PersonOutputDto> update(String id, PersonInputDto personInputDto);
}
