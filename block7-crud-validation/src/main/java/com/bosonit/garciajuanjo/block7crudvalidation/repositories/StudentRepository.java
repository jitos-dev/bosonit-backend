package com.bosonit.garciajuanjo.block7crudvalidation.repositories;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.Student;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {

    @Query(value = "SELECT id_student FROM students WHERE person_id = ?1 limit 1", nativeQuery = true)
    Optional<String> findStudentIdByPersonId(String personId);

    @Query(value = "SELECT * FROM students WHERE person_id = ?1 limit 1", nativeQuery = true)
    Optional<Student> findStudentByPersonId(String personId);

    /*Esta es una query para eliminar todos los Student por el id de su Person. Como es de
     * eliminación hay que anotarla con @Modifying y el método donde la utilicemos hay
     * que anotarlo con @Transactional para que se inicie una transacción para el borrado si o si*/
    @Modifying
    @Query(value = "DELETE FROM students WHERE person_id = ?1", nativeQuery = true)
    void deleteStudentByPersonId(String personId);

    @Modifying
    @Query(value = "DELETE FROM students WHERE id_student = ?1", nativeQuery = true)
    void deleteById(String studentId);

    @Query(value = "SELECT * FROM students WHERE person_id = ?1", nativeQuery = true)
    Optional<Student> findByPersonId(String personId);

    @Query(value = "SELECT * FROM students WHERE person_id IN ?1", nativeQuery = true)
    List<Student> findStudentsByPersonsIds(List<String> personIds);
}
