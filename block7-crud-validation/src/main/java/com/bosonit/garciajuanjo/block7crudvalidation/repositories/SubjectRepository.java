package com.bosonit.garciajuanjo.block7crudvalidation.repositories;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, String> {

    /*Esta es una query para eliminar todos los StudentSubject por el id de su Student. Como es de
     * eliminación hay que anotarla con @Modifying y el método donde la utilicemos hay
     * que anotarlo con @Transactional para que se inicie una transacción para el borrado si o si*/
    @Modifying
    @Query(value = "DELETE from subjects WHERE student_id = ?1", nativeQuery = true)
    void deleteSubjectByStudentId(String studentId);

    @Modifying
    @Query(value = "DELETE FROM student_subject WHERE subject_id = ?1", nativeQuery = true)
    void deleteStudentSubjectBySubjectId(String subjectId);

    @Modifying
    @Query(value = "DELETE FROM student_subject WHERE student_id = ?1", nativeQuery = true)
    void deleteStudentSubjectByStudentId(String studentId);

    @Modifying
    @Query(value = "DELETE FROM student_subject WHERE subject_id IN ?1 AND student_id = ?2", nativeQuery = true)
    void deleteStudentSubjectByStudentIdAndSubjectIds(List<String> subjectsId, String studentId);

    @Query(value = "SELECT * FROM subjects WHERE student_id = ?1", nativeQuery = true)
    List<Subject> findSubjectsByStudentId(String studentId);

    @Query(value = "SELECT * FROM subjects WHERE subject_name = ?1", nativeQuery = true)
    Optional<Subject> findSubjectBySubjectName(String subjectName);

    @Query(value = "SELECT COUNT(*) FROM subjects WHERE id_subject IN ?1", nativeQuery = true)
    Long countExistingSubjectsByIds(List<String> subjectIds);

    @Query(value = "SELECT * FROM subjects WHERE id_subject IN ?1", nativeQuery = true)
    List<Subject> findAllByIds(List<String> subjectIds);
}
