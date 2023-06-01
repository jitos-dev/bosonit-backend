package com.bosonit.garciajuanjo.block7crudvalidation.services.impl;

import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.EntityNotFoundException;
import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.UnprocessableEntityException;
import com.bosonit.garciajuanjo.block7crudvalidation.models.Student;
import com.bosonit.garciajuanjo.block7crudvalidation.models.Subject;
import com.bosonit.garciajuanjo.block7crudvalidation.models.SubjectName;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.SubjectInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.SubjectListOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.SubjectOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.SubjectSimpleOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.StudentRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.SubjectRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.services.SubjectService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private SubjectRepository subjectRepository;
    private StudentRepository studentRepository;

    @Override
    public List<SubjectOutputDto> findAll() {
        return subjectRepository.findAll()
                .stream()
                .map(Subject::subjectToSubjectOutputDto)
                .toList();
    }

    @Override
    public Optional<SubjectOutputDto> findById(String id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return Optional.of(subject.subjectToSubjectOutputDto());
    }

    @Override
    public Optional<SubjectListOutputDto> findByStudentId(String studentId) {
        Student student = studentRepository.findById(studentId)
                        .orElseThrow(EntityNotFoundException::new);

        List<SubjectSimpleOutputDto> subjects = student.getSubjects()
                .stream()
                .map(Subject::subjectToSubjectSimpleOutputDto)
                .toList();

        return Optional.of(new SubjectListOutputDto(student.studentToStudentSimpleOutputDto(), subjects));
    }


    @Override
    public Optional<SubjectOutputDto> save(SubjectInputDto inputDto) {
        //si el valor del campo subject_name no es válido
        if (!isValidSubjectName(inputDto.getSubjectName()))
            throw new UnprocessableEntityException("The value of subject name isn't valid");

        //comprobamos que no exista
        Optional<Subject> subject = subjectRepository.findSubjectBySubjectName(inputDto.getSubjectName());
        if (subject.isPresent())
                throw new UnprocessableEntityException("The subject already exist in the database");

        if (inputDto.getInitialDate() == null)
            throw new UnprocessableEntityException("The initial date field cannot be null");

        return Optional.of(subjectRepository
                .save(new Subject(inputDto))
                .subjectToSubjectOutputDto());
    }

    @Override
    public Optional<SubjectOutputDto> update(String id, SubjectInputDto inputDto) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new UnprocessableEntityException("The id of the subject doesn't correspond to any subject"));

        //si el valor del campo subject_name no es válido
        if (!isValidSubjectName(inputDto.getSubjectName()))
            throw new UnprocessableEntityException("The value of subject name isn't valid");

        //si quiere modificar el SubjectName pero ya existe en la base de datos
        Optional<Subject> subjectBySubjectName = subjectRepository.findSubjectBySubjectName(subject.getSubjectName().name());
        if (subjectBySubjectName.isPresent())
            throw new UnprocessableEntityException("The subject name already exist in the data base");

        Subject subjectUpdated = getStudentSubjectUpdated(subject, inputDto);

        return Optional.of(subjectRepository.save(subjectUpdated)
                .subjectToSubjectOutputDto());
    }

    @Transactional
    @Override
    public void delete(String id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        subjectRepository.deleteStudentSubjectBySubjectId(subject.getIdSubject());

        subjectRepository.delete(subject);
    }

    private Subject getStudentSubjectUpdated(Subject subject, SubjectInputDto inputDto) {
        subject.setSubjectName(SubjectName.valueOf(inputDto.getSubjectName()));
        subject.setComments(inputDto.getComments() == null ? subject.getComments() : inputDto.getComments());
        subject.setFinishDate(inputDto.getFinishDate() == null ? subject.getFinishDate() : inputDto.getFinishDate());
        return subject;
    }

    /**
     * Comprueba si el valor pasado es un valor correcto par el Enum SubjectName. Si al parsearlo con
     * valueOf no salta una excepción es que es válido.
     * @param subjectName String con el valor de SubjectName
     * @return true o false
     */
    private boolean isValidSubjectName(String subjectName) {
        try {
            SubjectName.valueOf(subjectName);
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
