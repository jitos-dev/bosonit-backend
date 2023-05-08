package com.bosonit.garciajuanjo.block7crudvalidation.services.impl;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.Person;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.Student;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.EntityNotFoundException;
import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.UnprocessableEntityException;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.PersonRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.StudentRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

    private StudentRepository studentRepository;

    private PersonRepository personRepository;

    @Override
    public List<StudentOutputDto> findAll() {
        return studentRepository
                .findAll().stream()
                .map(Student::studentToStudentOutputDto)
                .toList();
    }

    @Override
    public Optional<StudentOutputDto> getById(String id) {
        Optional<Student> optStudent = studentRepository.findById(id);

        if (optStudent.isEmpty())
            throw new EntityNotFoundException();

        return Optional.of(optStudent.get().studentToStudentOutputDto());
    }

    @Override
    public Optional<StudentOutputDto> save(StudentInputDto studentInputDto) {
        Optional<Person> optPerson = personRepository.findById(studentInputDto.getPerson().getIdPerson());

        if (optPerson.isEmpty())
            throw new UnprocessableEntityException("The id of the person doesn't correspond to any user");

        Optional<String> optStudentId = studentRepository.findStudentIdByPersonId(optPerson.get().getIdPerson());

        if (optStudentId.isPresent())
            throw new UnprocessableEntityException("The person's id is already associated with a student");

        /*Como al guardar un estudiante no pongo todos los campos del objeto (solo el id ya funciona) al
         * devolver el objeto Student los campos de person van a null.*/
        //todo preguntar si hay que buscar el objeto Person por su id para añadirlo a Student o solo devolver el id
        if (isAllFieldsCorrect(studentInputDto)) {
            return Optional.of(studentRepository
                    .save(new Student(studentInputDto))
                    .studentToStudentOutputDto());
        }

        return Optional.empty();
    }

    @Override
    public Optional<StudentOutputDto> update(String id, StudentInputDto inputDto) {
        Optional<Student> optStudent = studentRepository.findById(id);

        if (optStudent.isEmpty())
            throw new EntityNotFoundException();

        Student studentUpdated = getStudentUpdated(inputDto, optStudent.get());

        return Optional.of(studentRepository.save(studentUpdated).studentToStudentOutputDto());
    }

    @Override
    public void delete(String id) {
        Optional<Student> studentDatabase = studentRepository.findById(id);

        if (studentDatabase.isEmpty())
            throw new EntityNotFoundException();

        studentRepository.delete(studentDatabase.get());
    }


    private Boolean isAllFieldsCorrect(StudentInputDto dto) {
        if (dto == null)
            throw new UnprocessableEntityException("The object is null");

        if (dto.getNumHoursWeek() == null)
            throw new UnprocessableEntityException("The field numHoursWeek cannot be null");

        if (dto.getBranch() == null)
            throw new UnprocessableEntityException("The field branch cannot be null");

        return true;
    }

    private Student getStudentUpdated(StudentInputDto inputDto, Student studentDb) {
        studentDb.setBranch(inputDto.getBranch() == null ? studentDb.getBranch() : inputDto.getBranch());
        studentDb.setComments(inputDto.getComments() == null ? studentDb.getComments() : inputDto.getComments());
        studentDb.setNumHoursWeek(inputDto.getNumHoursWeek() == null ? studentDb.getNumHoursWeek() : inputDto.getNumHoursWeek());
        return studentDb;
    }
}