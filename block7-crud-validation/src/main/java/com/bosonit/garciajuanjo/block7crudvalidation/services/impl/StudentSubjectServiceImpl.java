package com.bosonit.garciajuanjo.block7crudvalidation.services.impl;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.Student;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.StudentSubject;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentSubjectInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentSubjectOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.EntityNotFoundException;
import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.UnprocessableEntityException;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.StudentRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.StudentSubjectRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.services.StudentSubjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentSubjectServiceImpl implements StudentSubjectService {

    private StudentSubjectRepository studentSubjectRepository;

    private StudentRepository studentRepository;

    @Override
    public List<StudentSubjectOutputDto> findAll() {
        return studentSubjectRepository.findAll()
                .stream()
                .map(StudentSubject::studentSubjectToStudentSubjectOutputDto)
                .toList();
    }

    @Override
    public Optional<StudentSubjectOutputDto> findById(String id) {
        Optional<StudentSubject> optStudentSubject = studentSubjectRepository.findById(id);

        if (optStudentSubject.isEmpty())
            throw new EntityNotFoundException();

        return Optional.of(optStudentSubject.get().studentSubjectToStudentSubjectOutputDto());
    }

    @Override
    public Optional<StudentSubjectOutputDto> save(StudentSubjectInputDto inputDto) {
        Optional<Student> optStudent = studentRepository.findById(inputDto.getStudent().getIdStudent());

        if (optStudent.isEmpty())
            throw new UnprocessableEntityException("The id of the student doesn't correspond to any student");

        if (inputDto.getInitialDate() == null)
            throw new UnprocessableEntityException("The initial date field cannot be null");

        // de esta forma como el Student lo obtenemos de bd lo tenemos con todos sus datos aunque esté vacio
        inputDto.setStudent(optStudent.get().studentToStudentInputDto());

        return Optional.of(studentSubjectRepository
                .save(new StudentSubject(inputDto))
                .studentSubjectToStudentSubjectOutputDto());
    }

    @Override
    public Optional<StudentSubjectOutputDto> update(String id, StudentSubjectInputDto inputDto) {
        Optional<StudentSubject> optStudentSubject = studentSubjectRepository.findById(id);

        if (optStudentSubject.isEmpty())
            throw new UnprocessableEntityException("The id of the student doesn't correspond to any student subject");

        StudentSubject studentSubjectDb = getStudentSubjectUpdated(optStudentSubject.get(), inputDto);

        return Optional.of(studentSubjectRepository.save(studentSubjectDb)
                .studentSubjectToStudentSubjectOutputDto());
    }

    @Override
    public void delete(String id) {
        Optional<StudentSubject> optStudentSubject = studentSubjectRepository.findById(id);

        if (optStudentSubject.isEmpty())
            throw new EntityNotFoundException();

        studentSubjectRepository.delete(optStudentSubject.get());
    }

    private StudentSubject getStudentSubjectUpdated(StudentSubject studentSubject, StudentSubjectInputDto inputDto) {
        studentSubject.setSubject(inputDto.getSubject() == null ? studentSubject.getSubject() : inputDto.getSubject());
        studentSubject.setComments(inputDto.getComments() == null ? studentSubject.getComments() : inputDto.getComments());
        studentSubject.setFinishDate(inputDto.getFinishDate() == null ? studentSubject.getFinishDate() : inputDto.getFinishDate());
        return studentSubject;
    }
}
