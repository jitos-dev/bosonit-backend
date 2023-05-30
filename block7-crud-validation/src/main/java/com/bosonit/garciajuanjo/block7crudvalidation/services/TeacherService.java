package com.bosonit.garciajuanjo.block7crudvalidation.services;

import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.TeacherInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.TeacherOutputDto;

import java.util.List;
import java.util.Optional;

public interface TeacherService {

    Optional<TeacherOutputDto> findById(String id);

    List<TeacherOutputDto> findAll();

    Optional<TeacherOutputDto> save(TeacherInputDto inputDto);

    Optional<TeacherOutputDto> update(String id, TeacherInputDto inputDto);

    void delete(String id);
}
