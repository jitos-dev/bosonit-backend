package com.bosonit.garciajuanjo.block7crudvalidation.services;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentSubjectInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentSubjectOutputDto;

import java.util.List;
import java.util.Optional;

public interface StudentSubjectService {

    List<StudentSubjectOutputDto> findAll();

    Optional<StudentSubjectOutputDto> findById(String id);

    Optional<StudentSubjectOutputDto> save(StudentSubjectInputDto inputDto);

    Optional<StudentSubjectOutputDto> update(String id, StudentSubjectInputDto inputDto);

    void delete(String id);
}
