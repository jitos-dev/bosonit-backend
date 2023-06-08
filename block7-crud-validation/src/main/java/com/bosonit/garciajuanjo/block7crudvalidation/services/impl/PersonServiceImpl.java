package com.bosonit.garciajuanjo.block7crudvalidation.services.impl;

import com.bosonit.garciajuanjo.block7crudvalidation.client.TeacherFeignClient;
import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.EntityNotFoundException;
import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.UnprocessableEntityException;
import com.bosonit.garciajuanjo.block7crudvalidation.models.OutputType;
import com.bosonit.garciajuanjo.block7crudvalidation.models.Person;
import com.bosonit.garciajuanjo.block7crudvalidation.models.Student;
import com.bosonit.garciajuanjo.block7crudvalidation.models.Teacher;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.PersonCompleteOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.PersonInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.PersonOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.TeacherOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.PersonRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.StudentRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.SubjectRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.TeacherRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.services.PersonService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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
    public List<PersonCompleteOutputDto> getAll(String output) {
        List<PersonOutputDto> persons = personRepository.findAll()
                .stream()
                .map(Person::personToPersonOutputDto).toList();

        OutputType outputType = OutputType.valueOf(output.toUpperCase());
        return getPersonCompleteOutputDto(outputType, persons);
    }

    @Override
    public List<PersonCompleteOutputDto> getAll() {
        List<PersonOutputDto> persons = personRepository.findAll()
                .stream()
                .map(Person::personToPersonOutputDto).toList();

        //Si el parámetro OutputType lo cambiamos a FULL nos devuelve todos los datos
        return getPersonCompleteOutputDto(OutputType.SIMPLE, persons);
    }

    @Override
    public PersonCompleteOutputDto getById(String id, String output) {
        Person person = personRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        List<PersonOutputDto> persons = Collections.singletonList(person.personToPersonOutputDto());
        OutputType outputType = OutputType.valueOf(output.toUpperCase());

        return getPersonCompleteOutputDto(outputType, persons)
                .stream().findFirst()
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public PersonCompleteOutputDto getByUser(String user, String output) {
        PersonOutputDto person = personRepository.findByUser(user)
                .orElseThrow(EntityNotFoundException::new)
                .personToPersonOutputDto();

        OutputType outputType = OutputType.valueOf(output.toUpperCase());

        return getPersonCompleteOutputDto(outputType, List.of(person))
                .stream()
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<PersonOutputDto> getBy(Map<String, Object> values) {
        return personRepository.findPersonsBy(values);
    }

    @Override
    public Optional<TeacherOutputDto> getTeacherByIdTeacher(String teacherId) {
        try {
            //Esta es la línea que hay que sustituir si queremos utilizar RestTemplate
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
        //comprobamos que no tenga referencias con algun Student porque si es así no se puede borrar
        if (teacher.isPresent() && !teacher.get().getStudents().isEmpty()) {
            throw new UnprocessableEntityException("The teacher cannot be deleted because it has associated Students");
        }

        personRepository.delete(person);
    }

    /**
     * Este método se encarga de cuando le pasemos una lista PersonOutputDto y el parámetro outputType si es FULL
     * comprobar cada una de esas 'person' si es un Teacher o Student y agregarle todos sus datos para devolver un
     * objeto de tipo PersonCompleteOutputDto
     *
     * @param outputType Enum con los valores FULL o SIMPLE
     * @param persons    List de PersonOutputDto
     * @return List de PersonCompleteOutputDto en función del parámetro outputType
     */
    public List<PersonCompleteOutputDto> getPersonCompleteOutputDto(OutputType outputType, List<PersonOutputDto> persons) {
        if (outputType == null || persons == null || persons.isEmpty())
            return new ArrayList<>();

        //Lista de salida
        List<PersonCompleteOutputDto> personCompleteList = new ArrayList<>();

        /*Lista de ids de persons. Lo hago de esta forma porque como reutilizo el método puedo estar buscando solo
         * un Person y así no tengo que traerme todos los Teachers o todos los Student*/
        List<String> personIds = persons.stream()
                .map(PersonOutputDto::getIdPerson)
                .toList();

        //Lista de Teachers que contengan los ids en la lista de ids de personas
        List<Teacher> teachers = new ArrayList<>();

        //Lista de Student que contengan los ids en la lista de ids de personas
        List<Student> students = new ArrayList<>();

        if (outputType == OutputType.FULL) {
            students.addAll(studentRepository.findStudentsByPersonsIds(personIds));
            teachers.addAll(teacherRepository.findTeachersByPersonsIds(personIds));
        }

        //Recorremos la lista de personas para ir añadiendo los datos
        persons.forEach(personOutputDto -> {
            //Creamos el objeto y añadimos la persona
            PersonCompleteOutputDto dto = new PersonCompleteOutputDto();
            dto.setPerson(personOutputDto);

            //Si viene el parámetro full le asignamos el resto de datos
            if (outputType == OutputType.FULL) {
                //Comprobamos si está en Students o en Teachers para añadirlo
                Optional<Teacher> teacher = teachers.stream()
                        .filter(teach -> teach.getPerson().getIdPerson().equals(personOutputDto.getIdPerson()))
                        .findFirst();

                Optional<Student> student = students.stream()
                        .filter(stud -> stud.getPerson().getIdPerson().equals(personOutputDto.getIdPerson()))
                        .findFirst();

                teacher.ifPresent(value -> dto.setTeacher(value.teacherToTeacherOutputDto()));

                student.ifPresent(value -> dto.setStudent(value.studentToStudentOutputDto()));
            }

            //añadimos el valor a la lista de salida
            personCompleteList.add(dto);
        });

        return personCompleteList;
    }

    public boolean isAllFieldsCorrect(PersonInputDto personInputDto) {
        if (personInputDto == null)
            throw new UnprocessableEntityException("The input of person cannot be null");

        if (personInputDto.getUser() == null)
            throw new UnprocessableEntityException("The user field cannot be null");

        if (personInputDto.getUser().length() < 6 || personInputDto.getUser().length() > 10)
            throw new UnprocessableEntityException("The user length cannot be less than 6 characters or greater than 10");

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

    public Person getPersonUpdated(PersonInputDto personInputDto, Person person) {
        //Check that inputs fields are valid
        checkInputsAreValid(personInputDto, person);

        person.setName(personInputDto.getName() == null ? person.getName() : personInputDto.getName());
        person.setUser(person.getUser());
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

    public void checkInputsAreValid(PersonInputDto personInputDto, Person person) {
        if (personInputDto == null || person == null)
            throw new UnprocessableEntityException("The inputs values cannot be null");

        if (personInputDto.getUser() == null)
            throw new UnprocessableEntityException("The user value of inputDto cannot be null");

        if (personInputDto.getUser().length() < 6)
            throw new UnprocessableEntityException("The user length cannot be less than 6 characters");

        if (personInputDto.getUser().length() > 10)
            throw new UnprocessableEntityException("The user length cannot be greater than 10 characters");
    }
}
