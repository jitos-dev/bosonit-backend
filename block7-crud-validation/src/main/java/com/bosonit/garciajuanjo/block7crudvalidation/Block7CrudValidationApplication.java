package com.bosonit.garciajuanjo.block7crudvalidation;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.*;
import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.PersonRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.StudentRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.SubjectRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.TeacherRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;;
import java.util.*;

@EnableFeignClients
@SpringBootApplication
public class Block7CrudValidationApplication {

    public static void main(String[] args) {
        SpringApplication.run(Block7CrudValidationApplication.class, args);
    }

    @Transactional
    @Bean
    CommandLineRunner commandLineRunner(String... args) {
        return value -> {
            //mockData();
/*            Person person = addPerson();
            Teacher teacher = addTeacher(person);
            Person personStudent = addPerson();
            Student student = addStudent(teacher, personStudent);
            Subject subject = addSubject();

            //le añadimos la asignatura al estudiante
            student.getSubjects().add(subject);
            studentRepository.save(student);*/
        };
    }

    private void mockData() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:mockData.json");
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = resource.getFile();

        PersonInputDto[] personList = objectMapper.readValue(jsonFile, PersonInputDto[].class);

        for (PersonInputDto person:personList) {
            personRepository.save(new Person(person));
        }
    }

    private Person addPerson() {

        PersonInputDto person = new PersonInputDto(
                null,
                "usuario",
                "123456",
                "juanjo",
                "Garcia",
                "juanjose.garcia@bosonit.com",
                "jitos86@gmail.com",
                "Sabiote",
                true,
                new Date(),
                "http://localhost:8080/imagenes/1",
                new Date());

        return personRepository.save(new Person(person));
    }

    private Teacher addTeacher(Person person) {
        Teacher teacher = new Teacher(
                null,
                "Comentarios del teacher",
                Branch.BACK,
                person,
                new ArrayList<>()
        );

        return teacherRepository.save(teacher);
    }

    private Student addStudent(Teacher teacher, Person person) {
        Student student = new Student(
                null,
                20,
                "Comentarios del estudiante",
                Branch.BACK,
                person,
                teacher,
                new HashSet<>()
        );

        return studentRepository.save(student);
    }

    private Subject addSubject() {
        Subject subject = new Subject(
                null,
                "Comentarios de la asignatura",
                new Date(),
                new Date(),
                SubjectName.HTML,
                new HashSet<>()
        );

        Subject subject2 = new Subject(
                null,
                "Comentarios de la asignatura",
                new Date(),
                new Date(),
                SubjectName.Angular,
                new HashSet<>()
        );

        Subject subject3 = new Subject(
                null,
                "Comentarios de la asignatura",
                new Date(),
                new Date(),
                SubjectName.CSS,
                new HashSet<>()
        );

        subjectRepository.save(subject2);
        subjectRepository.save(subject3);

        return subjectRepository.save(subject);
    }

    @Autowired
    public PersonRepository personRepository;

    @Autowired
    public TeacherRepository teacherRepository;

    @Autowired
    public StudentRepository studentRepository;

    @Autowired
    public SubjectRepository subjectRepository;

    @Autowired
    private ResourceLoader resourceLoader;
}
