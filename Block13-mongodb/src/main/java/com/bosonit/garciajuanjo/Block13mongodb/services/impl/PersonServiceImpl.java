package com.bosonit.garciajuanjo.Block13mongodb.services.impl;

import com.bosonit.garciajuanjo.Block13mongodb.exceptions.EntityNotFoundException;
import com.bosonit.garciajuanjo.Block13mongodb.exceptions.UnprocessableEntityException;
import com.bosonit.garciajuanjo.Block13mongodb.models.Person;
import com.bosonit.garciajuanjo.Block13mongodb.models.dtos.PersonInputDto;
import com.bosonit.garciajuanjo.Block13mongodb.models.dtos.PersonOutputDto;
import com.bosonit.garciajuanjo.Block13mongodb.services.PersonService;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
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
    public List<PersonOutputDto> findAll(Integer numberPage, Integer pageSize) {
        Query query = new Query();
        Pageable pageable = PageRequest.of(numberPage, pageSize);

        //Podemos aplicar filtros dinamicos como haciamos con Criteria API
        //query.addCriteria(Criteria.where("user").is("usuario10"));

        return mongoTemplate.find(query.with(pageable), Person.class)
                .stream()
                .map(Person::personToPersonOutputDto)
                .toList();
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

        person.setUser(inputDto.getUser() == null ? person.getUser() : inputDto.getUser());
        person.setPassword(inputDto.getPassword() == null ? person.getPassword() : inputDto.getPassword());
        person.setName(inputDto.getName() == null ? person.getName() : inputDto.getName());
        person.setSurname(inputDto.getSurname() == null ? person.getSurname() : inputDto.getSurname());
        person.setCompanyEmail(inputDto.getCompanyEmail() == null ? person.getCompanyEmail() : inputDto.getCompanyEmail());
        person.setPersonalEmail(inputDto.getPersonalEmail() == null ? person.getPersonalEmail() : inputDto.getPersonalEmail());
        person.setCity(inputDto.getCity() == null ? person.getCity() : inputDto.getCity());
        person.setActive(inputDto.getActive() == null ? person.getActive() : inputDto.getActive());
        person.setCreatedDate(inputDto.getCreatedDate() == null ? person.getCreatedDate() : inputDto.getCreatedDate());
        person.setImageUrl(inputDto.getImageUrl() == null ? person.getImageUrl() : inputDto.getImageUrl());
        person.setTerminationDate(inputDto.getTerminationDate() == null ? person.getTerminationDate() : inputDto.getTerminationDate());

        return mongoTemplate.save(person).personToPersonOutputDto();
    }

    @Override
    public void delete(String personId) {
        Person person = Optional.ofNullable(mongoTemplate.findById(personId, Person.class))
                .orElseThrow(() -> new EntityNotFoundException("Person not found fot this id: " + personId));

        DeleteResult result = mongoTemplate.remove(person);
        if (result.getDeletedCount() == 0)
            throw new UnprocessableEntityException("Person object with id " + personId + " could not be deleted",
                    HttpStatus.BAD_REQUEST);
    }

    @Override
    public PersonOutputDto save(PersonInputDto inputDto) {
        return Optional.of(mongoTemplate.save(new Person(inputDto)).personToPersonOutputDto())
                .orElseThrow(() ->
                        new UnprocessableEntityException("A problem has occurred and the record could not be saved"
                        , HttpStatus.BAD_REQUEST));
    }
}
