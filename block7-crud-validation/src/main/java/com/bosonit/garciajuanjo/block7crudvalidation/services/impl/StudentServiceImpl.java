package com.bosonit.garciajuanjo.block7crudvalidation.services.impl;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.Person;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.Student;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.StudentSubject;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.Teacher;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.*;
import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.EntityNotFoundException;
import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.UnprocessableEntityException;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.PersonRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.StudentRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.StudentSubjectRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.TeacherRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

    private StudentRepository studentRepository;
    private PersonRepository personRepository;
    private TeacherRepository teacherRepository;
    private StudentSubjectRepository subjectRepository;

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

        //TODO probar porque me sale el person como null
        //Dto para la salida
        PersonCompleteOutputDto outputDto = new PersonCompleteOutputDto();

        //Dto para el estudiante y su lista de asignaturas
        StudentAndSubjectsOutputDto studentSubjectsDto = new StudentAndSubjectsOutputDto();
        studentSubjectsDto.setStudent(student.studentToStudentSimpleOutputDto());

        //obtenemos las asignaturas del estudiantes, las mapeamos y las añadimos
        List<StudentSubjectSimpleOutputDto> subjects = subjectRepository.getSubjectsByIdStudent(student.getIdStudent())
                .stream()
                .map(StudentSubject::studentSubjectToStudentSubjectSimpleOutputDto)
                .toList();

        studentSubjectsDto.setSubjects(subjects);
        outputDto.setStudentAndSubjectsOutputDto(studentSubjectsDto);

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

        //Buscamos el student_id por el id de Person. Si existe es que ya esta asociado la Person con un Student
        Optional<String> studentId = studentRepository.findStudentIdByPersonId(person.getIdPerson());

        if (studentId.isPresent())
                throw new UnprocessableEntityException("The person's id is already associated with a student");

        //Buscamos el teacher_id por el id de Person. Si existe es que ya esta asociado la Person con un Teacher
        Optional<String> teacherId = teacherRepository.findTeacherIdFromIdPerson(studentInputDto.getPersonId());

        if (teacherId.isPresent())
                throw new UnprocessableEntityException("The person's id is already associated with a teacher");

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
    public void delete(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        studentRepository.delete(student);
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
