package com.bosonit.garciajuanjo.block7crudvalidation.services;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentOutputDto;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    Optional<StudentOutputDto> save(StudentInputDto studentInputDto);

    List<StudentOutputDto> findAll();

    Optional<StudentOutputDto> getById(String id);

    Optional<StudentOutputDto> update(String id, StudentInputDto inputDto);

    void delete(String id);
}
