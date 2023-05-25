package com.bosonit.garciajuanjo.block7crudvalidation.repositories;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.Person;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonOutputDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

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
                    Date parseDate = (Date) value;

                    if (criteria.equals(GREATER))
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(CREATED_DATE), (Date) value));

                    if (criteria.equals(LESS))
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(CREATED_DATE), (Date) value));

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

        /*Estos son datos para la paginación. Tenemos el numberPage que hace referencia al número de página por el que
        * queremos empezar. Empiezan por 0 como los arrays. Por otro lado tenemos pageSize que es el número de elementos
        * que queremos que tenga la página.
        * Con esto después de crear la query (en el return) establecemos setMaxResults con el valor de pageSize ya que
        * hace referencia al numero máximo de elementos que queremos devolver por página y por otro lado setFirstResult
        * que multiplicamos el numerPage por pageSize. En esta parte le indicamos en que elemento queremos empezar a
        * recuperar por lo que si por ejemplo el pageSize es 5 y numberPage 2 el resultado seria 10 por lo que nos
        * va a devolver empezando por el elemento 10 (incluido) un total de 5 elementos (hasta el 14)*/
        Integer numberPage = (Integer) values.get(NUMBER_PAGE);
        Integer pageSize = (Integer) values.get(PAGE_SIZE);

        query.select(root)
                .where(predicates.toArray(new Predicate[0]))
                .orderBy(orders);

        return entityManager
                .createQuery(query)
                .setMaxResults(pageSize)
                .setFirstResult(numberPage * pageSize)
                .getResultList()
                .stream()
                .map(Person::personToPersonOutputDto)
                .toList();
    }
}
