package com.bosonit.garciajuanjo.block7crudvalidation.services;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.Student;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.StudentOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Optional<StudentOutputDto> save(StudentInputDto studentInputDto) {
        return Optional.of(studentRepository.save(new Student(studentInputDto)).studentToStudentOutputDto());
    }
}
