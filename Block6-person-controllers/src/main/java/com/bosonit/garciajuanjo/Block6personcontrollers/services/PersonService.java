package com.bosonit.garciajuanjo.Block6personcontrollers.services;

import com.bosonit.garciajuanjo.Block6personcontrollers.entities.Person;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    public Person getPerson(String name, String population, String age) {
        return new Person(name, population, Integer.parseInt(age));
    }
}
