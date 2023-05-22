package com.bosonit.garciajuanjo.block7crudvalidation.services.impl;

import com.bosonit.garciajuanjo.block7crudvalidation.client.TeacherFeignClient;
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
import com.bosonit.garciajuanjo.block7crudvalidation.services.PersonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    private PersonRepository personRepository;
    private StudentRepository studentRepository;
    private SubjectRepository subjectRepository;
    private TeacherRepository teacherRepository;

    private TeacherFeignClient teacherFeignClient;

    @Override
    public List<PersonCompleteOutputDto> getAll(String outputType) {
        List<PersonOutputDto> persons = personRepository.findAll()
                .stream()
                .map(Person::personToPersonOutputDto).toList();

        return getPersonCompleteOutputDto(outputType, persons);
    }

    @Override
    public List<PersonCompleteOutputDto> getAll() {
        List<PersonOutputDto> persons = personRepository.findAll()
                .stream()
                .map(Person::personToPersonOutputDto).toList();

        //si cambio las comillas por "full" nos da los datos de si es profesor...
        return getPersonCompleteOutputDto("", persons);
    }

    @Override
    public Optional<PersonCompleteOutputDto> getById(String id, String outputType) {
        Person person = personRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        List<PersonOutputDto> persons = Collections.singletonList(person.personToPersonOutputDto());

        return getPersonCompleteOutputDto(outputType, persons).stream().findFirst();
    }

    @Override
    public List<PersonCompleteOutputDto> getByUser(String user, String outputType) {
        List<PersonOutputDto> personList = personRepository.findByUser(user)
                .stream()
                .map(Person::personToPersonOutputDto)
                .toList();

        if (personList.isEmpty())
            throw new EntityNotFoundException();

        return getPersonCompleteOutputDto(outputType, personList);
    }

    @Override
    public Optional<TeacherOutputDto> getTeacherByIdTeacher(String teacherId) {
        try {
/*            ResponseEntity<TeacherOutputDto> responseEntity = new RestTemplate()
                    .getForEntity("http://localhost:8081/teacher/" + teacherId, TeacherOutputDto.class);*/

            ResponseEntity<TeacherOutputDto> responseEntity = ResponseEntity.of(Optional.of(teacherFeignClient.getById(teacherId)));

            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                String message = "The answer wasn't correct." +
                        "\nHttpCode: " + responseEntity.getStatusCode();

                throw new UnprocessableEntityException(message);
            }

            return Optional.of(Objects.requireNonNull(responseEntity.getBody()));

        } catch (HttpClientErrorException hcee) {
            throw new EntityNotFoundException();

        } catch (Exception e) {
            throw new UnprocessableEntityException(e.getMessage());
        }
    }


    @Override
    public Optional<PersonOutputDto> save(PersonInputDto personInputDto) {
        if (isAllFieldsCorrect(personInputDto)) {
            return Optional.of(personRepository.save(new Person(personInputDto))
                    .personToPersonOutputDto());
        }

        return Optional.empty();
    }

    @Override
    public Optional<PersonOutputDto> update(String id, PersonInputDto personInputDto) {
        Person person = personRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        Person personUpdated = getPersonUpdated(personInputDto, person);

        return Optional.of(personRepository.save(personUpdated).personToPersonOutputDto());
    }


    @Transactional
    @Override
    public void delete(String id) {
        Person person = personRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        //Si es un Student primero tenemos que eliminar las referencias a Subject si las tuviera
        Optional<Student> student = studentRepository.findByPersonId(person.getIdPerson());
        if (student.isPresent()) {
            subjectRepository.deleteStudentSubjectByStudentId(student.get().getIdStudent());
            studentRepository.deleteStudentByPersonId(person.getIdPerson());
        }

        Optional<Teacher> teacher = teacherRepository.findTeacherFromPersonId(person.getIdPerson());
        if (teacher.isPresent()) {
            //comprobamos que no tenga referencias con algun Student porque si es así no se puede borrar
            if (!teacher.get().getStudents().isEmpty())
                throw new UnprocessableEntityException("The teacher cannot be deleted because it has associated Students");
        }

        personRepository.delete(person);
    }

    private List<PersonCompleteOutputDto> getPersonCompleteOutputDto(String outputType, List<PersonOutputDto> persons) {
        //Lista de salida
        List<PersonCompleteOutputDto> personCompleteList = new ArrayList<>();

        /*Lista de ids de persons. Lo hago de esta forma porque como reutilizo el método puedo estar buscando solo
         * un Person y así no tengo que traerme todos los Teachers o todos los Student*/
        List<String> personIds = persons.stream()
                .map(PersonOutputDto::getIdPerson)
                .toList();

        //Lista de Teachers que contengan el id en la lista de ids de personas
        List<Teacher> teachers = teacherRepository.findTeachersByPersonsIds(personIds);

        //Lista de Student que contengan el id en la lista de ids de personas
        List<Student> students = studentRepository.findStudentsByPersonsIds(personIds);

        persons.forEach(personOutputDto -> {
            //Creamos el objeto y añadimos la persona
            PersonCompleteOutputDto dto = new PersonCompleteOutputDto();
            dto.setPerson(personOutputDto);

            //Si viene el parámetro full le asignamos el resto de datos
            if (outputType.equalsIgnoreCase("full")) {
                //Comprobamos si está en Students o en Teachers para añadirlo
                Optional<Teacher> teacher = teachers.stream()
                        .filter(teach -> teach.getPerson().getIdPerson().equals(personOutputDto.getIdPerson()))
                        .findFirst();

                Optional<Student> student = students.stream()
                        .filter(stud -> stud.getPerson().getIdPerson().equals(personOutputDto.getIdPerson()))
                        .findFirst();

                teacher.ifPresent(value -> dto.setTeacherOutputDto(value.teacherToTeacherOutputDto()));

                student.ifPresent(value -> dto.setStudent(value.studentToStudentOutputDto()));
            }

            //añadimos el valor a la lista de salida
            personCompleteList.add(dto);
        });

        return personCompleteList;
    }

    private Boolean isAllFieldsCorrect(PersonInputDto personInputDto) {
        if (personInputDto.getUser() == null)
            throw new UnprocessableEntityException("The user field cannot be null");

        if (personInputDto.getUser().length() < 6 || personInputDto.getUser().length() > 10)
            throw new UnprocessableEntityException("The user length cannot be less than 6 characters or greater than 12");

        if (personInputDto.getPassword() == null)
            throw new UnprocessableEntityException("The password field cannot be null");

        if (personInputDto.getName() == null)
            throw new UnprocessableEntityException("The name field cannot be null");

        if (personInputDto.getCompanyEmail() == null)
            throw new UnprocessableEntityException("The company email field cannot be null");

        if (personInputDto.getPersonalEmail() == null)
            throw new UnprocessableEntityException("The personal email field cannot be null");

        if (personInputDto.getCity() == null)
            throw new UnprocessableEntityException("The city field cannot be null");

        if (personInputDto.getActive() == null)
            throw new UnprocessableEntityException("The active field cannot be null");

        if (personInputDto.getCreatedDate() == null)
            throw new UnprocessableEntityException("The created date field cannot be null");

        return true;
    }

    private Person getPersonUpdated(PersonInputDto personInputDto, Person person) {

        if (personInputDto.getUser() != null &&
                (personInputDto.getUser().length() < 6 || personInputDto.getUser().length() > 10))
            throw new UnprocessableEntityException("The user length cannot be less than 6 characters or greater than 12");


        person.setName(personInputDto.getName() == null ? person.getName() : personInputDto.getName());
        person.setUser(personInputDto.getUser() == null ? person.getUser() : personInputDto.getUser());
        person.setPassword(personInputDto.getPassword() == null ? person.getPassword() : personInputDto.getPassword());
        person.setSurname(personInputDto.getSurname() == null ? person.getSurname() : personInputDto.getSurname());
        person.setCompanyEmail(personInputDto.getCompanyEmail() == null ? person.getCompanyEmail() : personInputDto.getCompanyEmail());
        person.setPersonalEmail(personInputDto.getPersonalEmail() == null ? person.getPersonalEmail() : personInputDto.getPersonalEmail());
        person.setCity(personInputDto.getCity() == null ? person.getCity() : personInputDto.getCity());
        person.setActive(personInputDto.getActive() == null ? person.getActive() : personInputDto.getActive());
        person.setCreatedDate(personInputDto.getCreatedDate() == null ? person.getCreatedDate() : personInputDto.getCreatedDate());
        person.setImageUrl(personInputDto.getImageUrl() == null ? person.getImageUrl() : personInputDto.getImageUrl());
        person.setTerminationDate(personInputDto.getTerminationDate() == null ? person.getTerminationDate() : personInputDto.getTerminationDate());
        return person;
    }
}
