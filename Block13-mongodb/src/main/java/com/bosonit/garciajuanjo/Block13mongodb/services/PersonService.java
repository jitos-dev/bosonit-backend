package com.bosonit.garciajuanjo.Block13mongodb.services;

import com.bosonit.garciajuanjo.Block13mongodb.models.daos.PersonInputDto;
import com.bosonit.garciajuanjo.Block13mongodb.models.daos.PersonOutputDto;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    List<PersonOutputDto> findAll();

    List<PersonOutputDto> findAllPaginated(int pageNumber, int pageSize);

    Optional<PersonOutputDto> findById(Long personId);

    Optional<PersonOutputDto> update(PersonInputDto inputDto);

    void delete(Long personId);
}