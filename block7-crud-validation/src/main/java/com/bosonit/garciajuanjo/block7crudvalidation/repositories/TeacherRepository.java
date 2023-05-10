package com.bosonit.garciajuanjo.block7crudvalidation.repositories;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, String> {

    /*Esta es una query para eliminar todos los Teacher por el id de su Person. Como es de
    * eliminación hay que anotarla con @Modifying y el método donde la utilicemos hay
    * que anotarlo con @Transactional para que se inicie una transacción para el borrado si o si*/
    @Modifying
    @Query(value = "DELETE from teachers WHERE person_id = ?1", nativeQuery = true)
    void deleteTeacherByPersonId(String personId);

    @Query(value = "SELECT id_teacher from teachers WHERE person_id = ?1 limit 1", nativeQuery = true)
    Optional<String> findTeacherIdFromIdPerson(String id);

    @Query(value = "SELECT * from teachers WHERE person_id = ?1 limit 1", nativeQuery = true)
    Optional<Teacher> findTeacherFromPersonId(String personId);

    @Query(value = "SELECT * FROM teachers WHERE person_id IN ?1", nativeQuery = true)
    List<Teacher> findTeachersByPersonsIds(List<String> personIds);
}
