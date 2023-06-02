package com.bosonit.garciajuanjo.block7crudvalidation.services.impl;

import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.EntityNotFoundException;
import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.UnprocessableEntityException;
import com.bosonit.garciajuanjo.block7crudvalidation.models.Person;
import com.bosonit.garciajuanjo.block7crudvalidation.models.Teacher;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.TeacherInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.TeacherOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.PersonRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.StudentRepository;
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
    private StudentRepository studentRepository;

    @Override
    public Optional<TeacherOutputDto> findById(String id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return Optional.of(teacher.teacherToTeacherOutputDto());
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
        Person person = personRepository.findById(teacherInputDto.getPersonId())
                .orElseThrow(() -> new UnprocessableEntityException("The id of the person doesn't correspond to any user"));

        //comprobamos que no sea ya un Student
        Optional<String> studentId = studentRepository.findStudentIdByPersonId(person.getIdPerson());
        if (studentId.isPresent())
            throw new UnprocessableEntityException("The person's id is already associated with a student");

        //Comprobamos que no esté asociado ya a un Teacher
        Optional<String> teacherId = teacherRepository.findTeacherIdFromIdPerson(person.getIdPerson());
        if (teacherId.isPresent())
            throw new UnprocessableEntityException("The person's id is already associated with a teacher");

        //Si está correcto lo guardamos y lo devolvemos. Si hay algún problema se lanza una excepción
        checkIsAllFieldsCorrect(teacherInputDto);

        Teacher teacher = new Teacher(teacherInputDto);
        teacher.setPerson(person);

        Teacher teacherDb = teacherRepository.save(teacher);

        return Optional.of(teacherDb.teacherToTeacherOutputDto());
    }

    @Override
    public Optional<TeacherOutputDto> update(String id, TeacherInputDto inputDto) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        Teacher teacherUpdated = getTeacherUpdated(inputDto, teacher);
        return Optional.of(teacherRepository.save(teacherUpdated).teacherToTeacherOutputDto());
    }


    @Override
    public void delete(String id) {
        //no necesito guardar el Teacher, solo que si no lo encuentra que lance una excepcion
        Teacher teacher = teacherRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        teacherRepository.deleteById(teacher.getIdTeacher());
    }

    public void checkIsAllFieldsCorrect(TeacherInputDto inputDto) {
        if (inputDto == null)
            throw new UnprocessableEntityException("The object is null");

        if (inputDto.getBranch() == null)
            throw new UnprocessableEntityException("The field branch cannot be null");
    }

    public Teacher getTeacherUpdated(TeacherInputDto inputDto, Teacher teacherDb) {
        if (inputDto == null || teacherDb == null)
            throw new UnprocessableEntityException("The inputs values cannot be null");

        teacherDb.setBranch(inputDto.getBranch() == null ? teacherDb.getBranch() : inputDto.getBranch());
        teacherDb.setComments(inputDto.getComments() == null ? teacherDb.getComments() : inputDto.getComments());

        return teacherDb;
    }
}
