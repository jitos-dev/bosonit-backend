package com.bosonit.garciajuanjo.block7crudvalidation.controllers;

import com.bosonit.garciajuanjo.block7crudvalidation.models.Branch;
import com.bosonit.garciajuanjo.block7crudvalidation.models.Person;
import com.bosonit.garciajuanjo.block7crudvalidation.models.Teacher;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.*;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.PersonRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.TeacherRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.util.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) //para asignarle yo el puerto
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class TeacherControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private PersonRepository personRepository;

    private static String url;
    private List<Teacher> teachers;

    @BeforeAll
    void initValues() {
        Person person1 = Person.builder()
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

        Person person2 = Person.builder()
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

        Person personDB1 = personRepository.save(person1);
        Person personDB2 = personRepository.save(person2);

        Teacher teacher1 = Teacher.builder()
                .idTeacher(null)
                .comments("Comentarios del teacher")
                .branch(Branch.BACK)
                .person(personDB1)
                .students(new ArrayList<>())
                .build();

        Teacher teacher2 = Teacher.builder()
                .idTeacher(null)
                .comments("Comentarios del teacher")
                .branch(Branch.BACK)
                .person(personDB2)
                .students(new ArrayList<>())
                .build();

        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);

        //Guardamos una lista con los profesores para algunas consultas
        teachers = new ArrayList<>();
        teachers.add(teacher1);
        teachers.add(teacher2);

        url = "http://localhost:8080/teacher";
    }

    @DisplayName("Test for the getAll method")
    @Test
    void whenGetAll_returnListOfTeacherOutputDto() {
        //When
        RequestEntity<Void> request = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();
        ParameterizedTypeReference<List<TeacherOutputDto>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<TeacherOutputDto>> responseEntity = restTemplate.exchange(request, responseType);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));
        assertThat(responseEntity.getBody(), notNullValue());
        assertThat(Objects.requireNonNull(responseEntity.getBody()).size(), equalTo(2));
    }

    @DisplayName("Test for the getById method")
    @Test
    void whenGetById_returnTeacherOutputDto() {
        //Give
        Teacher teacher = teachers.get(0);
        String urlById = url + "/" + teacher.getIdTeacher();

        //When
        RequestEntity<Void> request = RequestEntity.get(urlById).accept(MediaType.APPLICATION_JSON).build();
        ParameterizedTypeReference<TeacherOutputDto> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<TeacherOutputDto> responseEntity = restTemplate.exchange(request, responseType);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));
        assertThat(responseEntity.getBody(), notNullValue());

        TeacherOutputDto teacherOutputDto = responseEntity.getBody();

        assertThat(teacherOutputDto.getComments(), equalTo(teacher.getComments()));
        assertThat(teacherOutputDto.getBranch(), equalTo(teacher.getBranch()));
        assertThat(teacherOutputDto.getIdTeacher(), equalTo(teacher.getIdTeacher()));
    }

    @DisplayName("Test for the addTeacher method")
    @Test
    void whenAddTeacher_returnTeacherOutputDto() {
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

        Person personTeacherDB = personRepository.save(personTeacher);
        TeacherInputDto teacherInputDto = TeacherInputDto.builder()
                .idTeacher(null)
                .comments("Comentarios del teacher")
                .branch(Branch.BACK)
                .personId(personTeacherDB.getIdPerson()).build();
        
        //When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TeacherInputDto> request = new HttpEntity<>(teacherInputDto, headers);
        ResponseEntity<TeacherOutputDto> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, TeacherOutputDto.class);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(201));
        assertThat(responseEntity.getBody(), notNullValue());

        TeacherOutputDto teacherOutputDto = responseEntity.getBody();
        assertThat(teacherOutputDto.getIdTeacher(), notNullValue());
        assertThat(teacherOutputDto.getComments(), equalTo("Comentarios del teacher"));
        assertThat(teacherOutputDto.getPerson().getIdPerson(), equalTo(personTeacherDB.getIdPerson()));
    }

    @DisplayName("Test for the update method")
    @Test
    void whenUpdate_returnTeacherOutputDto(){
        //Give
        Teacher teacher = teachers.get(0);
        TeacherInputDto teacherInputDto = TeacherInputDto.builder()
                .idTeacher(null)
                .comments("Comentarios del teacher modificados")
                .branch(Branch.FRONT).build();

        String updateUrl = url + "/" + teacher.getIdTeacher();

        //When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TeacherInputDto> request = new HttpEntity<>(teacherInputDto, headers);
        ResponseEntity<TeacherOutputDto> responseEntity = restTemplate.exchange(updateUrl, HttpMethod.PUT, request, TeacherOutputDto.class);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));
        assertThat(responseEntity.getBody(), notNullValue());

        TeacherOutputDto teacherOutputDto = responseEntity.getBody();
        assertThat(teacherOutputDto.getIdTeacher(), notNullValue());
        assertThat(teacherOutputDto.getComments(), equalTo("Comentarios del teacher modificados"));
        assertThat(teacherOutputDto.getBranch(), equalTo(Branch.FRONT));
    }

    @DisplayName("Test for the delete method")
    @Test
    void whenDelete_returnVoid(){
        //Give
        Teacher teacher = teachers.get(0);
        String deleteUrl = url + "/" + teacher.getIdTeacher();

        //When
        RequestEntity<Void> request = RequestEntity.delete(URI.create(deleteUrl)).build();
        ResponseEntity<Void> responseEntity = restTemplate.exchange(request, Void.class);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));

        Optional<Teacher> optional = teacherRepository.findById(teacher.getIdTeacher());

        assertThat(optional, notNullValue());
        assertFalse(optional.isPresent());
    }
}
