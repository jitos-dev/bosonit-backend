package com.bosonit.garciajuanjo.block7crudvalidation.services;

import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.UnprocessableEntityException;
import com.bosonit.garciajuanjo.block7crudvalidation.models.Branch;
import com.bosonit.garciajuanjo.block7crudvalidation.models.Person;
import com.bosonit.garciajuanjo.block7crudvalidation.models.Teacher;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.TeacherInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.TeacherOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.PersonRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.StudentRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.TeacherRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.services.impl.TeacherServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class TeacherServiceImplTest {

    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    private static TeacherInputDto teacherInputDto;
    private static Teacher teacher;
    private static Person personTeacher;

    @BeforeAll
    static void initValues() {
        personTeacher = Person.builder()
                .idPerson("1")
                .user("usuario3")
                .password("123456")
                .name("Pepe")
                .surname("Exposito")
                .companyEmail("pepe.garcia@bosonit.com")
                .personalEmail("pepe@gmail.com")
                .city("Ubeda")
                .active(true)
                .createdDate(new Date())
                .imageUrl("http://localhost:8080/imagen3")
                .terminationDate(new Date()).build();

        teacher = Teacher.builder()
                .idTeacher("1")
                .comments("comentarios")
                .branch(Branch.BACK)
                .person(personTeacher)
                .students(List.of()).build();

        teacherInputDto = TeacherInputDto.builder()
                .idTeacher("1")
                .comments("comentarios")
                .branch(Branch.BACK)
                .personId(personTeacher.getIdPerson()).build();
    }

    @DisplayName("Test for the getTeacherUpdated method")
    @Test
    void whenSave_returnOptionalOfTeacherOutputDto() {
        //When
        //Caso1: Cuando no encuentra un Person salta una excepción
        Mockito.when(personRepository.findById("1")).thenReturn(Optional.empty());

        //Caso2: Cuando buscamos el id del estudiante por el id de Person y salta la excepción
        Mockito.when(personRepository.findById("2")).thenReturn(Optional.of(personTeacher));
        Mockito.when(studentRepository.findStudentIdByPersonId("2")).thenReturn(Optional.of("2"));

        //Caso3: Cuando buscamos el id del profesor por el id de Person y salta la excepción
        Mockito.when(personRepository.findById("3")).thenReturn(Optional.of(personTeacher));
        Mockito.when(studentRepository.findStudentIdByPersonId("3")).thenReturn(Optional.empty());
        Mockito.when(teacherRepository.findTeacherIdFromIdPerson("3")).thenReturn(Optional.of("3"));

        //Caso bueno
        Mockito.when(personRepository.findById("4")).thenReturn(Optional.of(personTeacher));
        Mockito.when(studentRepository.findStudentIdByPersonId("4")).thenReturn(Optional.empty());
        Mockito.when(teacherRepository.findTeacherIdFromIdPerson("4")).thenReturn(Optional.empty());

        //La parte de save la tengo que hacer de la siguiente forma porque me da un error de que no es el mismo objeto
        //el que se guarda que el que se devuelve. La opción del error la dejo comentada
        //Mockito.when(teacherRepository.save(teacherDB)).thenReturn(teacherDB);
        Teacher teacherDB = new Teacher(teacherInputDto);
        teacherDB.setPerson(personTeacher);
        ArgumentCaptor<Teacher> teacherCaptor = ArgumentCaptor.forClass(Teacher.class);
        doReturn(teacherDB).when(teacherRepository).save(teacherCaptor.capture());

        //Then
        //Caso1
        assertThrows(UnprocessableEntityException.class, ()-> teacherService.save(teacherInputDto));

        //Caso2
        teacherInputDto.setPersonId("2");
        personTeacher.setIdPerson("2");
        assertThrows(UnprocessableEntityException.class, ()-> teacherService.save(teacherInputDto));

        //Caso3
        teacherInputDto.setPersonId("3");
        personTeacher.setIdPerson("3");
        assertThrows(UnprocessableEntityException.class, ()-> teacherService.save(teacherInputDto));

        //Caso bueno
        teacher.getPerson().setIdPerson("4");
        Optional<TeacherOutputDto> optional = teacherService.save(teacher.teacherToTeacherInputDto());

        assertThat(optional, notNullValue());
        assertTrue(optional.isPresent());
    }

    @DisplayName("Test for the getTeacherUpdated method")
    @Test
    void whenGetTeacherUpdated_returnTeacher() {
        //Then
        //Caso cuando los parámetros de entrada son null
        assertThrows(UnprocessableEntityException.class, ()-> teacherService.getTeacherUpdated(null, null));

        //Caso1: Cuando getBranch es null
        teacherInputDto.setBranch(null);
        Teacher teacher1 = teacherService.getTeacherUpdated(teacherInputDto, teacher);

        assertThat(teacher1, notNullValue());
        assertThat(teacher1.getBranch(), notNullValue());
        assertThat(teacher1.getBranch(), equalTo(Branch.BACK));

        //Caso2: Cuando getComment es null
        teacherInputDto.setComments(null);
        Teacher teacher2 = teacherService.getTeacherUpdated(teacherInputDto, teacher);

        assertThat(teacher2, notNullValue());
        assertThat(teacher2.getComments(), notNullValue());
        assertThat(teacher1.getComments(), equalTo("comentarios"));
    }
}
