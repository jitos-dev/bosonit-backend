package com.bosonit.garciajuanjo.block7crudvalidation.repositories;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.Person;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonOutputDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface PersonRepository extends JpaRepository<Person, String> {

    List<Person> findByUser(String user);

    List<PersonOutputDto> findPersonsBy(Map<String, Object> values);
}
