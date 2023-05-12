package com.bosonit.garciajuanjo.block7crudvalidation.services;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonCompleteOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.TeacherOutputDto;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    Optional<PersonOutputDto> save(PersonInputDto personInputDto);

    Optional<PersonCompleteOutputDto> getById(String id, String outputType);

    List<PersonCompleteOutputDto> getByUser(String name, String outputType);

    List<PersonCompleteOutputDto> getAll(String outputType);

    Optional<TeacherOutputDto> getTeacherByIdTeacher(String teacherId);

    Optional<PersonOutputDto> update(String id, PersonInputDto personInputDto);

    void delete(String id);


}
