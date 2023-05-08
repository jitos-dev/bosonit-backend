package com.bosonit.garciajuanjo.block7crudvalidation.repositories;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {

    @Query(value = "SELECT id_student FROM students WHERE person_id = ?1 limit 1", nativeQuery = true)
    Optional<String> findStudentIdByPersonId(String personId);
}
