package com.bosonit.garciajuanjo.block7crudvalidation.services.impl;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.Student;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.Subject;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentSubjectInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentSubjectListOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentSubjectOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentSubjectSimpleOutputDto;
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
                .map(Subject::studentSubjectToStudentSubjectOutputDto)
                .toList();
    }

    @Override
    public Optional<StudentSubjectOutputDto> findById(String id) {
        Subject subject = studentSubjectRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return Optional.of(subject.studentSubjectToStudentSubjectOutputDto());
    }

    @Override
    public Optional<StudentSubjectListOutputDto> findByStudentId(String studentId) {
        Student student = studentRepository.findById(studentId)
                        .orElseThrow(EntityNotFoundException::new);

        List<StudentSubjectSimpleOutputDto> subjects = studentSubjectRepository
                .findSubjectsByStudentId(studentId)
                .stream()
                .map(Subject::studentSubjectToStudentSubjectSimpleOutputDto)
                .toList();

        return Optional.of(new StudentSubjectListOutputDto(student.studentToStudentSimpleOutputDto(), subjects));
    }


    @Override
    public Optional<StudentSubjectOutputDto> save(StudentSubjectInputDto inputDto) {
        Student student = studentRepository.findById(inputDto.getStudentId())
                .orElseThrow(() -> new UnprocessableEntityException("The id of the student doesn't correspond to any student"));

        if (inputDto.getInitialDate() == null)
            throw new UnprocessableEntityException("The initial date field cannot be null");

        Subject subject = new Subject(inputDto);
        subject.setStudent(student);

        return Optional.of(studentSubjectRepository
                .save(subject)
                .studentSubjectToStudentSubjectOutputDto());
    }

    @Override
    public Optional<StudentSubjectOutputDto> update(String id, StudentSubjectInputDto inputDto) {
        Subject subject = studentSubjectRepository.findById(id)
                .orElseThrow(() -> new UnprocessableEntityException("The id of the student doesn't correspond to any student subject"));

        Subject subjectUpdated = getStudentSubjectUpdated(subject, inputDto);

        return Optional.of(studentSubjectRepository.save(subjectUpdated)
                .studentSubjectToStudentSubjectOutputDto());
    }

    @Override
    public void delete(String id) {
        Subject subject = studentSubjectRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        studentSubjectRepository.delete(subject);
    }

    private Subject getStudentSubjectUpdated(Subject subject, StudentSubjectInputDto inputDto) {
        subject.setSubjectName(inputDto.getSubjectName() == null ? subject.getSubjectName() : inputDto.getSubjectName());
        subject.setComments(inputDto.getComments() == null ? subject.getComments() : inputDto.getComments());
        subject.setFinishDate(inputDto.getFinishDate() == null ? subject.getFinishDate() : inputDto.getFinishDate());
        return subject;
    }
}
