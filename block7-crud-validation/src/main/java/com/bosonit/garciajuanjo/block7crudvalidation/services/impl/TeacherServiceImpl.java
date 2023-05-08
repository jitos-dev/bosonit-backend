package com.bosonit.garciajuanjo.block7crudvalidation.services.impl;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.Person;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.Teacher;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.TeacherInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.TeacherOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.EntityNotFoundException;
import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.UnprocessableEntityException;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.PersonRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.TeacherRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.services.TeacherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private TeacherRepository teacherRepository;

    private PersonRepository personRepository;

    @Override
    public Optional<TeacherOutputDto> findById(String id) {
        Optional<Teacher> optTeacher = teacherRepository.findById(id);

        if (optTeacher.isEmpty())
            throw new EntityNotFoundException();

        return Optional.of(optTeacher.get().teacherToTeacherOutputDto());
    }

    @Override
    public List<TeacherOutputDto> findAll() {
        return teacherRepository.findAll()
                .stream()
                .map(Teacher::teacherToTeacherOutputDto)
                .toList();
    }

    @Override
    public Optional<TeacherOutputDto> save(TeacherInputDto teacherInputDto) {
        Optional<Person> optPerson = personRepository.findById(teacherInputDto.getPerson().getIdPerson());

        if (optPerson.isEmpty())
            throw new UnprocessableEntityException("The id of the person doesn't correspond to any user");

        Optional<String> teacherId = teacherRepository.findTeacherIdFromIdPerson(optPerson.get().getIdPerson());

        if (teacherId.isPresent())
            throw new UnprocessableEntityException("The person's id is already associated with a teacher");

        if (isAllFieldsCorrect(teacherInputDto))
            return Optional.of(teacherRepository.save(new Teacher(teacherInputDto)).teacherToTeacherOutputDto());

        return Optional.empty();
    }

    @Override
    public Optional<TeacherOutputDto> update(String id, TeacherInputDto inputDto) {
        Optional<Teacher> optTeacher = teacherRepository.findById(id);

        if (optTeacher.isEmpty())
            throw new EntityNotFoundException();

        Teacher teacherUpdated = getTeacherUpdated(inputDto, optTeacher.get());
        return Optional.of(teacherRepository.save(teacherUpdated).teacherToTeacherOutputDto());
    }


    @Override
    public void delete(String id) {
        Optional<Teacher> optTeacher = teacherRepository.findById(id);

        if (optTeacher.isEmpty())
            throw new EntityNotFoundException();

        teacherRepository.delete(optTeacher.get());
    }

    private Boolean isAllFieldsCorrect(TeacherInputDto dto) {
        if (dto == null)
            throw new UnprocessableEntityException("The object is null");

        if (dto.getBranch() == null)
            throw new UnprocessableEntityException("The field branch cannot be null");

        return true;
    }

    private Teacher getTeacherUpdated(TeacherInputDto inputDto, Teacher teacherDb) {
        teacherDb.setBranch(inputDto.getBranch() == null ? teacherDb.getBranch() : inputDto.getBranch());
        teacherDb.setComments(inputDto.getComments() == null ? teacherDb.getComments() : inputDto.getComments());

        return teacherDb;
    }
}
