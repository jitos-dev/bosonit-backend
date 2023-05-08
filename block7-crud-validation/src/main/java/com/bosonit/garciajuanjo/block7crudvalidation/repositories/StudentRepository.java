package com.bosonit.garciajuanjo.block7crudvalidation.repositories;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, String> {
}
