package com.bosonit.garciajuanjo.block7crudvalidation.services;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.Person;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
    public List<PersonOutputDto> getPersonsByUser(String user) {
        return repository.findByUser(user).stream()
                .map(Person::personToPersonOutputDto)
                .toList();
    }

    @Override
    public Optional<PersonOutputDto> save(PersonInputDto personInputDto) throws Exception {
            if (isAllFieldsCorrect(personInputDto)) {
                return Optional.of(repository.save(new Person(personInputDto))
                        .personToPersonOutputDto());
            }

            return Optional.empty();
    }

    @Override
    public Optional<PersonOutputDto> update(int id, PersonInputDto personInputDto) {
        Optional<Person> optPerson = repository.findById(id);

        if (optPerson.isEmpty())
            return Optional.empty();

        Person person = optPerson.get();
        person.setIdPerson(id);

        Person personUpdated = getPersonUpdated(personInputDto, person);

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

    private Boolean isAllFieldsCorrect(PersonInputDto personInputDto) throws Exception {
        if (personInputDto.getUser() == null)
            throw new Exception("The user field cannot be null");

        if (personInputDto.getUser().length() < 6 || personInputDto.getUser().length() > 10)
            throw new Exception("The user length cannot be less than 6 characters or greater than 12");

        if (personInputDto.getPassword() == null)
            throw new Exception("The password field cannot be null");

        if (personInputDto.getName() == null)
            throw new Exception("The name field cannot be null");

        if (personInputDto.getCompanyEmail() == null)
            throw new Exception("The company email field cannot be null");

        if (personInputDto.getPersonalEmail() == null)
            throw new Exception("The personal email field cannot be null");

        if (personInputDto.getCity() == null)
            throw new Exception("The city field cannot be null");

        if (personInputDto.getActive() == null)
            throw new Exception("The active field cannot be null");

        if (personInputDto.getCreatedDate() == null)
            throw new Exception("The created date field cannot be null");

        return true;
    }

    private Person getPersonUpdated(PersonInputDto personInputDto, Person person) {
        person.setName(personInputDto.getName() == null ? person.getName() : personInputDto.getName());
        person.setUser(personInputDto.getUser() == null ? person.getUser() : personInputDto.getUser());
        person.setPassword(personInputDto.getPassword() == null ? person.getPassword() : personInputDto.getPassword());
        person.setSurname(personInputDto.getSurname() == null ? person.getSurname() : personInputDto.getSurname());
        person.setCompanyEmail(personInputDto.getCompanyEmail() == null ? person.getCompanyEmail() : personInputDto.getCompanyEmail());
        person.setPersonalEmail(personInputDto.getPersonalEmail() == null ? person.getPersonalEmail() : personInputDto.getPersonalEmail());
        person.setCity(personInputDto.getCity() == null ? person.getCity() : personInputDto.getCity());
        person.setActive(personInputDto.getActive() == null ? person.getActive() : personInputDto.getActive());
        person.setCreatedDate(personInputDto.getCreatedDate() == null ? person.getCreatedDate() : personInputDto.getCreatedDate());
        person.setImageUrl(personInputDto.getImageUrl() == null ? person.getImageUrl() : personInputDto.getImageUrl());
        person.setTerminationDate(personInputDto.getTerminationDate() == null ? person.getTerminationDate() : personInputDto.getTerminationDate());
        return person;
    }
}
