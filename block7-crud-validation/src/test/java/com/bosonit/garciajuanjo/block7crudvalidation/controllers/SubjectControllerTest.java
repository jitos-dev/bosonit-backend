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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) //para asignarle yo el puerto
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SubjectControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private StudentRepository studentRepository;

    private static String url;
    private List<Subject> subjects;
    private Student studentDB;

    @BeforeAll
    void initValues(){
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

        subjects = new ArrayList<>();
        subjects.add(subjectDB1);
        subjects.add(subjectDB2);

        //Para probar el de getByStudentId
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

        Person personStudent = Person.builder()
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

        Person personDBStudent = personRepository.save(personStudent);

        Student student = Student.builder()
                .numHoursWeek(20)
                .comments("Comentarios del estudiante 1")
                .branch(Branch.BACK)
                .person(personDBStudent)
                .teacher(teacherDB)
                .subjects(new HashSet<>()).build();

        studentDB = studentRepository.save(student);

        url = "http://localhost:8080/subject";
    }

    @Order(1)
    @DisplayName("Test for the getAll method")
    @Test
    void whenGetAll_returnListOfSubjectOutputDto() {
        //When
        RequestEntity<Void> request = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();
        ParameterizedTypeReference<List<SubjectOutputDto>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<SubjectOutputDto>> responseEntity = restTemplate.exchange(request, responseType);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));
        assertThat(responseEntity.getBody(), notNullValue());
        assertThat(Objects.requireNonNull(responseEntity.getBody()).size(), equalTo(2));
    }

    @Order(2)
    @DisplayName("Test for the getById method")
    @Test
    void whenGetById_returnSubjectOutputDto() {
        //Give
        Subject subject = subjects.get(0);
        String urlById = url + "/" + subject.getIdSubject();

        //When
        RequestEntity<Void> request = RequestEntity.get(urlById).accept(MediaType.APPLICATION_JSON).build();
        ParameterizedTypeReference<SubjectOutputDto> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<SubjectOutputDto> responseEntity = restTemplate.exchange(request, responseType);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));
        assertThat(responseEntity.getBody(), notNullValue());

        SubjectOutputDto subjectOutputDto = responseEntity.getBody();

        assertThat(subjectOutputDto.getIdStudentSubject(), equalTo(subject.getIdSubject()));
        assertThat(subjectOutputDto.getComments(), equalTo(subject.getComments()));
        assertThat(subjectOutputDto.getSubjectName(), equalTo(subject.getSubjectName()));
    }

    @Order(3)
    @DisplayName("Test for the getByUserId method")
    @Test
    void whenGetByStudentId_returnSubjectListOutputDto() {
        //Give
        studentDB.getSubjects().add(subjects.get(0));
        studentDB.getSubjects().add(subjects.get(1));
        studentRepository.save(studentDB);

        String urlByStudentId = url + "/student/" + studentDB.getIdStudent();

        //When
        RequestEntity<Void> request = RequestEntity.get(urlByStudentId).accept(MediaType.APPLICATION_JSON).build();
        ParameterizedTypeReference<SubjectListOutputDto> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<SubjectListOutputDto> responseEntity = restTemplate.exchange(request, responseType);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));
        assertThat(responseEntity.getBody(), notNullValue());

        SubjectListOutputDto subjectListOutputDto = responseEntity.getBody();

        assertFalse(subjectListOutputDto.getSubjects().isEmpty());
        assertThat(subjectListOutputDto.getStudent().getIdStudent(), equalTo(studentDB.getIdStudent()));
        assertThat(subjectListOutputDto.getSubjects().size(), equalTo(2));
    }

    @Order(4)
    @DisplayName("Test for the addSubject method")
    @Test
    void whenAddSubject_returnSubjectOutputDto() {
        //Give
        SubjectInputDto subjectInputDto = SubjectInputDto.builder()
                .subjectId(null)
                .subjectName("SpringBoot")
                .comments("Comentarios de SpringBoot")
                .initialDate(new Date())
                .finishDate(new Date()).build();


        //When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SubjectInputDto> request = new HttpEntity<>(subjectInputDto, headers);
        ResponseEntity<SubjectOutputDto> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, SubjectOutputDto.class);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(201));
        assertThat(responseEntity.getBody(), notNullValue());

        SubjectOutputDto subjectOutputDto = responseEntity.getBody();
        assertThat(subjectOutputDto.getIdStudentSubject(), notNullValue());
        assertThat(subjectOutputDto.getSubjectName().name(), equalTo("SpringBoot"));
        assertThat(subjectOutputDto.getComments(), equalTo("Comentarios de SpringBoot"));
    }

    @Order(5)
    @DisplayName("Test for the update method")
    @Test
    void whenUpdate_returnSubjectOutputDto(){
        //Give
        Subject subject = subjects.get(0);
        SubjectInputDto subjectInputDto = SubjectInputDto.builder()
                .subjectName("JavaScript")
                .comments("Comentarios de JavaScript")
                .initialDate(new Date())
                .finishDate(new Date()).build();

        String updateUrl = url + "/" + subject.getIdSubject();

        //When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SubjectInputDto> request = new HttpEntity<>(subjectInputDto, headers);
        ResponseEntity<SubjectOutputDto> responseEntity = restTemplate.exchange(updateUrl, HttpMethod.PUT, request, SubjectOutputDto.class);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));
        assertThat(responseEntity.getBody(), notNullValue());

        SubjectOutputDto subjectOutputDto = responseEntity.getBody();

        assertThat(subjectOutputDto.getIdStudentSubject(), notNullValue());
        assertThat(subjectOutputDto.getSubjectName().name(), equalTo("JavaScript"));
        assertThat(subjectOutputDto.getComments(), equalTo("Comentarios de JavaScript"));
    }

    @Order(6)
    @DisplayName("Test for the delete method")
    @Test
    void whenDelete_returnVoid(){
        //Give
        Subject subject = subjects.get(0);
        String deleteUrl = url + "/" + subject.getIdSubject();

        //When
        RequestEntity<Void> request = RequestEntity.delete(URI.create(deleteUrl)).build();
        ResponseEntity<Void> responseEntity = restTemplate.exchange(request, Void.class);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));

        Optional<Subject> optional = subjectRepository.findById(subject.getIdSubject());

        assertFalse(optional.isPresent());
    }
}
