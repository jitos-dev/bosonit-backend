package com.bosonit.garciajuanjo.block7crudvalidation.services.impl;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.Person;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.Student;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.Subject;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.Teacher;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.*;
import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.EntityNotFoundException;
import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.UnprocessableEntityException;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.PersonRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.StudentRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.SubjectRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.TeacherRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.services.StudentService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

    private StudentRepository studentRepository;
    private PersonRepository personRepository;
    private TeacherRepository teacherRepository;
    private SubjectRepository subjectRepository;

    @Override
    public List<StudentOutputDto> findAll() {
        return studentRepository
                .findAll().stream()
                .map(Student::studentToStudentOutputDto)
                .toList();
    }

    @Override
    public Optional<PersonCompleteOutputDto> getById(String id, String outputType) {
        Student student = studentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        //Dto para la salida
        PersonCompleteOutputDto outputDto = new PersonCompleteOutputDto();
        outputDto.setStudent(student.studentToStudentOutputDto());

        //si outputType es FULL
        if (outputType.equalsIgnoreCase("full")) {
            Person person = personRepository.findById(student.getPerson().getIdPerson())
                    .orElseThrow(EntityNotFoundException::new);

            outputDto.setPerson(person.personToPersonOutputDto());
        }

        return Optional.of(outputDto);
    }

    @Override
    public Optional<StudentOutputDto> save(StudentInputDto studentInputDto) {
        //Comprobamos que el idPerson corresponde con algun Person
        Person person = personRepository.findById(studentInputDto.getPersonId())
                .orElseThrow(() -> new UnprocessableEntityException("The id of the person doesn't correspond to any user"));

        //Comprobamos que no sea ya un Student
        Optional<String> studentId = studentRepository.findStudentIdByPersonId(person.getIdPerson());
        if (studentId.isPresent())
                throw new UnprocessableEntityException("The person's id is already associated with a student");

        //Comprobamos que no sea ya un Teacher
        Optional<String> teacherId = teacherRepository.findTeacherIdFromIdPerson(studentInputDto.getPersonId());
        if (teacherId.isPresent())
                throw new UnprocessableEntityException("The person's id is already associated with a teacher");

        //Comprobamos que el teacherId existe en la base de datos asociado a algún Teacher
        Teacher teacher = teacherRepository.findById(studentInputDto.getTeacherId())
                .orElseThrow(() -> new UnprocessableEntityException("The id of the teacher doesn't correspond any record"));

        if (isAllFieldsCorrect(studentInputDto)) {
            Student student = new Student(studentInputDto);
            student.setPerson(person);
            student.setTeacher(teacher);

            return Optional.of(studentRepository
                    .save(student)
                    .studentToStudentOutputDto());
        }

        return Optional.empty();
    }



    @Override
    public Optional<StudentOutputDto> update(String id, StudentInputDto inputDto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        Student studentUpdated = getStudentUpdated(inputDto, student);

        return Optional.of(studentRepository.save(studentUpdated).studentToStudentOutputDto());
    }

    @Override
    public Optional<StudentOutputDto> addSubjects(List<String> subjectsIds, String studentId) {
        if (subjectsIds.isEmpty())
            throw new UnprocessableEntityException("There are not subjects to associate the student with");

        Student student = studentRepository.findById(studentId)
                .orElseThrow(EntityNotFoundException::new);

        Long count = subjectRepository.countExistingSubjectsByIds(subjectsIds);
        if (count != subjectsIds.size())
            throw new UnprocessableEntityException("The ids provided do not correspond to the subjects");

        List<Subject> subjects = subjectRepository.findAllByIds(subjectsIds);
        student.getSubjects().addAll(subjects);

        return Optional.of(studentRepository.save(student).studentToStudentOutputDto());
    }

    @Transactional
    @Override
    public void deleteSubjects(List<String> subjectsIds, String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(EntityNotFoundException::new);

        List<Subject> subjects = subjectRepository.findAllByIds(subjectsIds);
        subjects.forEach(student.getSubjects()::remove);

        studentRepository.save(student);
    }

    @Transactional
    @Override
    public void delete(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        //Modificamos Student antes para poder borrarlo
        student.getSubjects().clear();
        studentRepository.save(student);

        studentRepository.deleteStudentByPersonId(student.getPerson().getIdPerson());
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
