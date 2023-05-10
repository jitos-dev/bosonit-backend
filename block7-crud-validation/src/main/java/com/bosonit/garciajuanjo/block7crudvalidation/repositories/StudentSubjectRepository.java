package com.bosonit.garciajuanjo.block7crudvalidation.repositories;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.StudentSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentSubjectRepository extends JpaRepository<StudentSubject, String> {

    /*Esta es una query para eliminar todos los StudentSubject por el id de su Student. Como es de
     * eliminación hay que anotarla con @Modifying y el método donde la utilicemos hay
     * que anotarlo con @Transactional para que se inicie una transacción para el borrado si o si*/
    @Modifying
    @Query(value = "DELETE from student_subject WHERE student_id = ?1", nativeQuery = true)
    void deleteStudentSubjectByStudentId(String studentId);

    @Query(value = "SELECT * FROM student_subject WHERE student_id = ?1", nativeQuery = true)
    List<StudentSubject> getSubjectsByIdStudent(String studentId);
}
