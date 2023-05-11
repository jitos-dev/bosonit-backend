package com.bosonit.garciajuanjo.block7crudvalidation.services;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.SubjectInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.SubjectListOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.SubjectOutputDto;

import java.util.List;
import java.util.Optional;

public interface SubjectService {

    List<SubjectOutputDto> findAll();

    Optional<SubjectOutputDto> findById(String id);

    Optional<SubjectListOutputDto> findByStudentId(String studentId);

    Optional<SubjectOutputDto> save(SubjectInputDto inputDto);

    Optional<SubjectOutputDto> update(String id, SubjectInputDto inputDto);

    void delete(String id);
}
