package com.bosonit.garciajuanjo.Block13mongodb.services.impl;

import com.bosonit.garciajuanjo.Block13mongodb.models.daos.PersonInputDto;
import com.bosonit.garciajuanjo.Block13mongodb.models.daos.PersonOutputDto;
import com.bosonit.garciajuanjo.Block13mongodb.services.PersonService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {
    @Override
    public List<PersonOutputDto> findAll() {
        return null;
    }

    @Override
    public List<PersonOutputDto> findAllPaginated(int pageNumber, int pageSize) {
        return null;
    }

    @Override
    public Optional<PersonOutputDto> findById(Long personId) {
        return Optional.empty();
    }

    @Override
    public Optional<PersonOutputDto> update(PersonInputDto inputDto) {
        return Optional.empty();
    }

    @Override
    public void delete(Long personId) {

    }
}
