package com.bosonit.garciajuanjo.block7crudvalidation.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class PersonRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;
}
