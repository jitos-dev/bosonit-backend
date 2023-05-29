package com.bosonit.garciajuanjo.Block13mongodb.services.impl;

import com.bosonit.garciajuanjo.Block13mongodb.exceptions.EntityNotFoundException;
import com.bosonit.garciajuanjo.Block13mongodb.exceptions.UnprocessableEntityException;
import com.bosonit.garciajuanjo.Block13mongodb.models.Person;
import com.bosonit.garciajuanjo.Block13mongodb.models.dtos.PersonInputDto;
import com.bosonit.garciajuanjo.Block13mongodb.models.dtos.PersonOutputDto;
import com.bosonit.garciajuanjo.Block13mongodb.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<PersonOutputDto> findAll() {
        return mongoTemplate.findAll(Person.class)
                .stream()
                .map(Person::personToPersonOutputDto)
                .toList();
    }

    @Override
    public List<PersonOutputDto> findAllPaginated(int pageNumber, int pageSize) {
        return null;
    }

    @Override
    public PersonOutputDto findById(String personId) {
        return Optional.ofNullable(mongoTemplate.findById(personId, Person.class))
                .orElseThrow(EntityNotFoundException::new)
                .personToPersonOutputDto();
    }

    @Override
    public PersonOutputDto update(PersonInputDto inputDto) {
        Person person = Optional.ofNullable(mongoTemplate.findById(inputDto.getIdPerson(), Person.class))
                .orElseThrow(() -> new EntityNotFoundException("Person not found for this id: " + inputDto.getIdPerson()));

        person.setUser(inputDto.getUser());
        person.setPassword(inputDto.getPassword());
        person.setName(inputDto.getName());
        person.setSurname(inputDto.getSurname());
        person.setCompanyEmail(inputDto.getCompanyEmail());
        person.setPersonalEmail(inputDto.getPersonalEmail());
        person.setCity(inputDto.getCity());
        person.setActive(inputDto.getActive());
        person.setCreatedDate(inputDto.getCreatedDate());
        person.setImageUrl(inputDto.getImageUrl());
        person.setTerminationDate(inputDto.getTerminationDate());

        return mongoTemplate.save(person).personToPersonOutputDto();
    }

    @Override
    public void delete(String personId) {

    }

    @Override
    public PersonOutputDto save(PersonInputDto inputDto) {
        return Optional.of(mongoTemplate.save(new Person(inputDto)).personToPersonOutputDto())
                .orElseThrow(()->
                        new UnprocessableEntityException("A problem has occurred and the record could not be saved"));
    }
}
