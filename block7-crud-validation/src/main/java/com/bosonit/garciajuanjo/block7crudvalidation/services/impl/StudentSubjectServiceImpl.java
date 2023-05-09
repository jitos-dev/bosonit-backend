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
        StudentSubject studentSubject = studentSubjectRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return Optional.of(studentSubject.studentSubjectToStudentSubjectOutputDto());
    }

    @Override
    public Optional<StudentSubjectOutputDto> save(StudentSubjectInputDto inputDto) {
        Student student = studentRepository.findById(inputDto.getStudentId())
                .orElseThrow(() -> new UnprocessableEntityException("The id of the student doesn't correspond to any student"));

        if (inputDto.getInitialDate() == null)
            throw new UnprocessableEntityException("The initial date field cannot be null");

        StudentSubject studentSubject = new StudentSubject(inputDto);
        studentSubject.setStudent(student);

        return Optional.of(studentSubjectRepository
                .save(studentSubject)
                .studentSubjectToStudentSubjectOutputDto());
    }

    @Override
    public Optional<StudentSubjectOutputDto> update(String id, StudentSubjectInputDto inputDto) {
        StudentSubject studentSubject = studentSubjectRepository.findById(id)
                .orElseThrow(() -> new UnprocessableEntityException("The id of the student doesn't correspond to any student subject"));

        StudentSubject studentSubjectUpdated = getStudentSubjectUpdated(studentSubject, inputDto);

        return Optional.of(studentSubjectRepository.save(studentSubjectUpdated)
                .studentSubjectToStudentSubjectOutputDto());
    }

    @Override
    public void delete(String id) {
        StudentSubject studentSubject = studentSubjectRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        studentSubjectRepository.delete(studentSubject);
    }

    private StudentSubject getStudentSubjectUpdated(StudentSubject studentSubject, StudentSubjectInputDto inputDto) {
        studentSubject.setSubject(inputDto.getSubject() == null ? studentSubject.getSubject() : inputDto.getSubject());
        studentSubject.setComments(inputDto.getComments() == null ? studentSubject.getComments() : inputDto.getComments());
        studentSubject.setFinishDate(inputDto.getFinishDate() == null ? studentSubject.getFinishDate() : inputDto.getFinishDate());
        return studentSubject;
    }
}
