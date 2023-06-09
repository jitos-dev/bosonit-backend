package com.bosonit.garciajuanjo.block7crud.services;

import com.bosonit.garciajuanjo.block7crud.entities.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    Optional<Person> save(Person person);

    Optional<Person> getPersonById(Long id);

    List<Person> getPersonsByName(String name);

    List<Person> getAll();

    Optional<Person> delete(Long id);

    Optional<Person> update(Long id, Person person);
}
