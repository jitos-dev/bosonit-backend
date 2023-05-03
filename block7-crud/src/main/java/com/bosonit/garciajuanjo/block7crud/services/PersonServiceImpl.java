package com.bosonit.garciajuanjo.block7crud.services;

import com.bosonit.garciajuanjo.block7crud.entities.Person;
import com.bosonit.garciajuanjo.block7crud.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService{

    @Autowired
    private PersonRepository repository;

    @Override
    public Optional<Person> save(Person person) {
        return Optional.of(repository.save(person));
    }

    @Override
    public Optional<Person> getPersonById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Person> getAll() {
        return repository.findAll();
    }
}
