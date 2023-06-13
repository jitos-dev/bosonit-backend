package com.bosonit.garciajuanjo.block7crudvalidation.repositories;

import com.bosonit.garciajuanjo.block7crudvalidation.models.Person;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.PersonOutputDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, String> {

    Optional<Person> findByUser(String user);

    List<PersonOutputDto> findPersonsBy(Map<String, Object> values);

    Optional<Person> findByPersonalEmail(String personalEmail);
}
