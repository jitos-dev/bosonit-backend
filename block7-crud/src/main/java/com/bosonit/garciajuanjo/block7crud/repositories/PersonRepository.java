package com.bosonit.garciajuanjo.block7crud.repositories;

import com.bosonit.garciajuanjo.block7crud.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByName(String name);
}
