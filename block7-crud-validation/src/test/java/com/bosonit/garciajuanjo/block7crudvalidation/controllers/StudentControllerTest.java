package com.bosonit.garciajuanjo.block7crudvalidation.controllers;

import com.bosonit.garciajuanjo.block7crudvalidation.models.*;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.*;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.PersonRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.StudentRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.SubjectRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.TeacherRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) //para asignarle yo el puerto
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    private static String url;

    private List<Student> students;

    @BeforeAll
    void initValues() {
        //Lo limpiamos todo primero
        subjectRepository.deleteAll();
        subjectRepository.deleteAll();
        teacherRepository.deleteAll();
        personRepository.deleteAll();

        Person personTeacher = Person.builder()
                .idPerson(null)
                .user("usuario1")
                .password("123456")
                .name("juanjo")
                .surname("garcia")
                .companyEmail("juanjose.garcia@bosonit.com")
                .personalEmail("jitos86@gmail.com")
                .city("Sabiote")
                .active(true)
                .createdDate(new Date())
                .imageUrl("http://localhost:8080/imagen1")
                .terminationDate(new Date()).build();

        Person personDBTeacher = personRepository.save(personTeacher);

        Teacher teacher = Teacher.builder()
                .idTeacher(null)
                .comments("Comentarios del teacher")
                .branch(Branch.BACK)
                .person(personDBTeacher)
                .students(new ArrayList<>())
                .build();

        Teacher teacherDB = teacherRepository.save(teacher);

        Person personStudent1 = Person.builder()
                .idPerson(null)
                .user("usuario2")
                .password("123456")
                .name("maria")
                .surname("garcia")
                .companyEmail("maria.garcia@bosonit.com")
                .personalEmail("maria@gmail.com")
                .city("Ubeda")
                .active(true)
                .createdDate(new Date())
                .imageUrl("http://localhost:8080/imagen1")
                .terminationDate(new Date()).build();

        Person personStudent2 = Person.builder()
                .idPerson(null)
                .user("usuario3")
                .password("123456")
                .name("pepe")
                .surname("garcia")
                .companyEmail("pepe.garcia@bosonit.com")
                .personalEmail("pepe@gmail.com")
                .city("Jaén")
                .active(true)
                .createdDate(new Date())
                .imageUrl("http://localhost:8080/imagen1")
                .terminationDate(new Date()).build();

        Person personDBStudent1 = personRepository.save(personStudent1);
        Person personDBStudent2 = personRepository.save(personStudent2);

        Student student1 = Student.builder()
                .numHoursWeek(20)
                .comments("Comentarios del estudiante 1")
                .branch(Branch.BACK)
                .person(personDBStudent1)
                .teacher(teacherDB).build();

        Student student2 = Student.builder()
                .numHoursWeek(20)
                .comments("Comentarios del estudiante 1")
                .branch(Branch.BACK)
                .person(personDBStudent2)
                .teacher(teacherDB).build();

        Student studentDB1 = studentRepository.save(student1);
        Student studentDB2 = studentRepository.save(student2);

        //Guardamos una lista con los estudiantes para algunas consultas
        students = new ArrayList<>();
        students.add(studentDB1);
        students.add(studentDB2);

        url = "http://localhost:8080/student";
    }

    @Order(1)
    @DisplayName("Test for the getAll method")
    @Test
    void whenGetAll_returnListOfStudentOutputDto() {
        //When
        RequestEntity<Void> request = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();
        ParameterizedTypeReference<List<StudentOutputDto>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<StudentOutputDto>> responseEntity = restTemplate.exchange(request, responseType);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));
        assertThat(responseEntity.getBody(), notNullValue());
        assertThat(Objects.requireNonNull(responseEntity.getBody()).size(), equalTo(2));
    }

    @Order(2)
    @DisplayName("Test for the getById method")
    @Test
    void whenGetById_returnStudentOutputDto() {
        //Give
        Student student = students.get(0);
        String urlById = url + "/" + student.getIdStudent();

        //When
        RequestEntity<Void> request = RequestEntity.get(urlById).accept(MediaType.APPLICATION_JSON).build();
        ParameterizedTypeReference<PersonCompleteOutputDto> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<PersonCompleteOutputDto> responseEntity = restTemplate.exchange(request, responseType);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));
        assertThat(responseEntity.getBody(), notNullValue());

        PersonCompleteOutputDto personCompleteOutputDto = responseEntity.getBody();

        assertThat(personCompleteOutputDto.getStudent().getIdStudent(), equalTo(student.getIdStudent()));
        assertThat(personCompleteOutputDto.getStudent().getBranch(), equalTo(student.getBranch()));
        assertThat(personCompleteOutputDto.getPerson(), nullValue()); //es null porque lo llamo con simple
        assertThat(personCompleteOutputDto.getTeacher(), nullValue()); //es null porque lo llamo con simple
    }

    @Order(3)
    @DisplayName("Test for the addStudent method")
    @Test
    void whenAddStudent_returnStudentOutputDto() {
        //Give
        Person personTeacher = Person.builder()
                .idPerson(null)
                .user("usuario1")
                .password("123456")
                .name("juanjo")
                .surname("garcia")
                .companyEmail("juanjose.garcia@bosonit.com")
                .personalEmail("jitos86@gmail.com")
                .city("Sabiote")
                .active(true)
                .createdDate(new Date())
                .imageUrl("http://localhost:8080/imagen1")
                .terminationDate(new Date()).build();

        Person personStudent = Person.builder()
                .idPerson(null)
                .user("usuario2")
                .password("123456")
                .name("ana")
                .surname("garcia")
                .companyEmail("ana.garcia@bosonit.com")
                .personalEmail("ana@gmail.com")
                .city("Madrid")
                .active(true)
                .createdDate(new Date())
                .imageUrl("http://localhost:8080/imagen1")
                .terminationDate(new Date()).build();

        Person personTeacherDB = personRepository.save(personTeacher);
        Person personStudentDB = personRepository.save(personStudent);

        Teacher teacher = Teacher.builder()
                .idTeacher(null)
                .comments("Comentarios del teacher")
                .branch(Branch.BACK)
                .person(personTeacherDB)
                .students(new ArrayList<>())
                .build();

        Teacher teacherDB = teacherRepository.save(teacher);

        StudentInputDto studentInputDto = StudentInputDto.builder()
                .numHoursWeek(25)
                .comments("Comentarios del estudiante")
                .branch(Branch.BACK)
                .personId(personStudentDB.getIdPerson())
                .teacherId(teacherDB.getIdTeacher()).build();

        //When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<StudentInputDto> request = new HttpEntity<>(studentInputDto, headers);
        ResponseEntity<StudentOutputDto> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, StudentOutputDto.class);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(201));
        assertThat(responseEntity.getBody(), notNullValue());

        StudentOutputDto studentOutputDto = responseEntity.getBody();

        assertThat(studentOutputDto.getIdStudent(), notNullValue());
        assertThat(studentOutputDto.getPerson().getIdPerson(), equalTo(personStudentDB.getIdPerson()));
        assertThat(studentOutputDto.getTeacher().getIdTeacher(), equalTo(teacherDB.getIdTeacher()));
    }

    @Order(4)
    @DisplayName("Test for the addSubject method")
    @Test
    void whenAddSubject_returnStudentOutputDto() {
        //Give
        Subject subject1 = Subject.builder()
                .idSubject(null)
                .comments("comentario subject 1")
                .initialDate(new Date())
                .finishDate(new Date())
                .subjectName(SubjectName.HTML).build();

        Subject subject2 = Subject.builder()
                .idSubject(null)
                .comments("comentario subject 2")
                .initialDate(new Date())
                .finishDate(new Date())
                .subjectName(SubjectName.Angular).build();

        Subject subjectDB1 = subjectRepository.save(subject1);
        Subject subjectDB2 = subjectRepository.save(subject2);

        List<String> subjectsIds = new ArrayList<>();
        subjectsIds.add(subjectDB1.getIdSubject());
        subjectsIds.add(subjectDB2.getIdSubject());

        Student student = students.get(0);

        String addSubjectUrl = url + "/subject/" + student.getIdStudent();

        //When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<String>> request = new HttpEntity<>(subjectsIds, headers);
        ResponseEntity<StudentOutputDto> responseEntity = restTemplate.exchange(addSubjectUrl, HttpMethod.PUT, request, StudentOutputDto.class);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));
        assertThat(responseEntity.getBody(), notNullValue());

        StudentOutputDto studentOutputDto = responseEntity.getBody();

        assertThat(studentOutputDto, notNullValue());
        assertThat(studentOutputDto.getSubjects(), notNullValue());
        assertThat(studentOutputDto.getSubjects().size(), equalTo(2));
    }


    @Order(5)
    @DisplayName("Test for the update method")
    @Test
    void whenUpdate_returnTeacherOutputDto(){
        //Give
        Student student = students.get(0);

        StudentInputDto studentInputDto = StudentInputDto.builder()
                .numHoursWeek(25)
                .comments("Comentarios del estudiante modificado")
                .branch(Branch.FRONT).build();

        String updateUrl = url + "/" + student.getIdStudent();

        //When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<StudentInputDto> request = new HttpEntity<>(studentInputDto, headers);
        ResponseEntity<StudentOutputDto> responseEntity = restTemplate.exchange(updateUrl, HttpMethod.PUT, request, StudentOutputDto.class);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));
        assertThat(responseEntity.getBody(), notNullValue());

        StudentOutputDto studentOutputDto = responseEntity.getBody();
        assertThat(studentOutputDto, notNullValue());
        assertThat(studentOutputDto.getTeacher(), notNullValue());
        assertThat(studentOutputDto.getPerson(), notNullValue());
        assertThat(studentOutputDto.getComments(), equalTo("Comentarios del estudiante modificado"));
        assertThat(studentOutputDto.getBranch(), equalTo(Branch.FRONT));
    }

    @Order(6)
    @DisplayName("Test for the delete method")
    @Test
    void whenDelete_returnVoid(){
        //Give
        Student student = students.get(0);
        String deleteUrl = url + "/" + student.getIdStudent();

        //When
        RequestEntity<Void> request = RequestEntity.delete(URI.create(deleteUrl)).build();
        ResponseEntity<Void> responseEntity = restTemplate.exchange(request, Void.class);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));

        Optional<Student> optional = studentRepository.findById(student.getIdStudent());

        assertThat(optional, notNullValue());
        assertFalse(optional.isPresent());
    }
}
