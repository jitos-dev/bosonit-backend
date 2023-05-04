package com.bosonit.garciajuanjo.block7crudvalidation.services;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.Person;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    private PersonRepository repository;

    @Override
    public List<PersonOutputDto> getAll() {
        return repository.findAll()
                .stream()
                .map(Person::personToPersonOutputDto).toList();
    }

    @Override
    public Optional<PersonOutputDto> getPersonById(int id) {
        Optional<Person> person = repository.findById(id);

        return person.map(Person::personToPersonOutputDto);
    }

    @Override
    public List<PersonOutputDto> getPersonsByName(String name) {
        return repository.findByName(name).stream()
                .map(Person::personToPersonOutputDto)
                .toList();
    }

    @Override
    public Optional<PersonOutputDto> save(PersonInputDto personInputDto) {
        return Optional.of(repository.save(new Person(personInputDto))
                .personToPersonOutputDto());
    }

    @Override
    public Optional<PersonOutputDto> update(int id, PersonInputDto personInputDto) {
        Optional<Person> optPerson = repository.findById(id);

        if (optPerson.isEmpty())
            return Optional.empty();

        Person personUpdated = optPerson.get();
        personUpdated.setIdPerson(id);
        personUpdated.setName(personInputDto.getName() == null ? personUpdated.getName() : personInputDto.getName());
        personUpdated.setUser(personInputDto.getUser() == null ? personUpdated.getUser() : personInputDto.getUser());
        personUpdated.setPassword(personInputDto.getPassword() == null ? personUpdated.getPassword() : personInputDto.getPassword());
        personUpdated.setSurname(personInputDto.getSurname() == null ? personUpdated.getSurname() : personInputDto.getSurname());
        personUpdated.setCompanyEmail(personInputDto.getCompanyEmail() == null ? personUpdated.getCompanyEmail() : personInputDto.getCompanyEmail());
        personUpdated.setPersonalEmail(personInputDto.getPersonalEmail() == null ? personUpdated.getPersonalEmail() : personInputDto.getPersonalEmail());
        personUpdated.setCity(personInputDto.getCity() == null ? personUpdated.getCity() : personInputDto.getCity());
        personUpdated.setActive(personInputDto.getActive() == null ? personUpdated.getActive() : personInputDto.getActive());
        personUpdated.setCreatedDate(personInputDto.getCreatedDate() == null ? personUpdated.getCreatedDate() : personInputDto.getCreatedDate());
        personUpdated.setImageUrl(personInputDto.getImageUrl() == null ? personUpdated.getImageUrl() : personInputDto.getImageUrl());
        personUpdated.setTerminationDate(personInputDto.getTerminationDate() == null ? personUpdated.getTerminationDate() : personInputDto.getTerminationDate());

        return Optional.of(repository.save(personUpdated).personToPersonOutputDto());
    }


    @Override
    public Optional<PersonOutputDto> delete(int id) {
        Optional<Person> person = repository.findById(id);

        if (person.isEmpty())
            return Optional.empty();

        repository.delete(person.get());

        return person.map(Person::personToPersonOutputDto);
    }
}
