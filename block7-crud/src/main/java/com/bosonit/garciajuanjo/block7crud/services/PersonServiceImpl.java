package com.bosonit.garciajuanjo.block7crud.services;

import com.bosonit.garciajuanjo.block7crud.entities.Person;
import com.bosonit.garciajuanjo.block7crud.repositories.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService{

    private PersonRepository repository;

    @Override
    public List<Person> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Person> getPersonById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Person> getPersonsByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Optional<Person> save(Person person) {
        return Optional.of(repository.save(person));
    }

    @Override
    public Optional<Person> update(Long id, Person person) {
        Optional<Person> optPerson = repository.findById(id);

        if (optPerson.isEmpty())
            return optPerson;

        Person personUpdated = optPerson.get();
        personUpdated.setIdPerson(id);
        personUpdated.setName(person.getName() == null ? personUpdated.getName() : person.getName());
        personUpdated.setAge(person.getAge() == null ? personUpdated.getAge() : person.getAge());
        personUpdated.setPopulation(person.getPopulation() == null ? personUpdated.getPopulation() : person.getPopulation());

        return Optional.of(repository.save(personUpdated));
    }

    @Override
    public Optional<Person> delete(Long id) {
        Optional<Person> person = repository.findById(id);

        person.ifPresent(value -> repository.delete(value));

        return person;
    }
}
