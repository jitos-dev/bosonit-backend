package com.bosonit.garciajuanjo.block7crudvalidation.repositories;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.Person;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonOutputDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.bosonit.garciajuanjo.block7crudvalidation.utils.Constants.*;

public class PersonRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public List<PersonOutputDto> findPersonsBy(Map<String, Object> values) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> query = criteriaBuilder.createQuery(Person.class);
        Root<Person> root = query.from(Person.class);

        List<Predicate> predicates = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        values.forEach((key, value) -> {
            switch (key) {
                case NAME -> predicates.add(criteriaBuilder.like(root.get(NAME), "%" + value + "%"));

                case USER -> predicates.add(criteriaBuilder.like(root.get(USER), "%" + value + "%"));

                case SURNAME -> predicates.add(criteriaBuilder.like(root.get(SURNAME), "%" + value + "%"));

                case CREATED_DATE -> {
                    String criteria = values.get(GREATER_OR_LESS).toString();
                    Date parseDate = dateStringToDate(value.toString());

                    if (criteria.equals(GREATER))
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(CREATED_DATE), parseDate));

                    if (criteria.equals(LESS))
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(CREATED_DATE), parseDate));

                }
                case ORDER_BY_NAME -> {
                    Boolean orderByName = (Boolean) values.get(ORDER_BY_NAME);

                    if (orderByName)
                        orders.add(criteriaBuilder.asc(root.get(NAME)));
                }
                case ORDER_BY_USER -> {
                    Boolean orderByUser = (Boolean) values.get(ORDER_BY_USER);

                    if (orderByUser)
                        orders.add(criteriaBuilder.asc(root.get(USER)));
                }
            }
        });

        query.select(root)
                .where(predicates.toArray(new Predicate[0]))
                .orderBy(orders);

        return entityManager
                .createQuery(query)
                .getResultList()
                .stream()
                .map(Person::personToPersonOutputDto)
                .toList();
    }

    private Date dateStringToDate(String value) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE);
        //Esto es para que tenga que cumplir el formato exacto
        dateFormat.setLenient(false);

        try {
            return dateFormat.parse(value);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
