package com.bosonit.garciajuanjo.block7crudvalidation.services;

import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.PersonCompleteOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.PersonInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.PersonOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.TeacherOutputDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PersonService {

    Optional<PersonOutputDto> save(PersonInputDto personInputDto);

    PersonCompleteOutputDto getById(String id, String outputType);

    PersonCompleteOutputDto getByUser(String name, String outputType);

    List<PersonCompleteOutputDto> getAll(String outputType);

    List<PersonCompleteOutputDto> getAll();

    List<PersonOutputDto> getBy(Map<String , Object> values);

    Optional<TeacherOutputDto> getTeacherByIdTeacher(String teacherId);

    Optional<PersonOutputDto> update(String id, PersonInputDto personInputDto);

    void delete(String id);


}
