package com.bosonit.garciajuanjo.block7crudvalidation.services;

import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.*;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    Optional<StudentOutputDto> save(StudentInputDto studentInputDto);

    List<StudentOutputDto> findAll();

    Optional<PersonCompleteOutputDto> getById(String id, String outputType);

    Optional<StudentOutputDto> update(String id, StudentInputDto inputDto);

    Optional<StudentOutputDto> addSubjects(List<String> subjectsIds, String studentId);

    void deleteSubjects(List<String> subjectsIds, String studentId);

    void delete(String id);
}
