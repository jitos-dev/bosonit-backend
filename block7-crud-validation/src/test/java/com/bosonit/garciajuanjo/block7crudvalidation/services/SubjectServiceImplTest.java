package com.bosonit.garciajuanjo.block7crudvalidation.services;

import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.EntityNotFoundException;
import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.UnprocessableEntityException;
import com.bosonit.garciajuanjo.block7crudvalidation.models.*;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.SubjectInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.SubjectListOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.SubjectOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.StudentRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.SubjectRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.services.impl.SubjectServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) //esto es para que las reglas no sean tan estrictas
class SubjectServiceImplTest {

    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private SubjectServiceImpl subjectService;

    private static SubjectInputDto subjectInput;
    private static Subject subject;
    private static Student student;

    @BeforeAll
    static void initValues() {
        subjectInput = SubjectInputDto.builder()
                .subjectId("1")
                .subjectName("Angular")
                .comments("Comentarios subject input")
                .initialDate(new Date())
                .finishDate(new Date()).build();

        subject = Subject.builder()
                .idSubject("1")
                .subjectName(SubjectName.Angular)
                .comments("Comentarios subject")
                .initialDate(new Date())
                .finishDate(new Date())
                .students(new HashSet<>()).build();

        Person personStudent = Person.builder()
                .idPerson("st")
                .user("usuario2")
                .password("123456")
                .name("Maria")
                .surname("Padilla")
                .companyEmail("maria.garcia@bosonit.com")
                .personalEmail("maria@gmail.com")
                .city("Sabiote")
                .active(true)
                .createdDate(new Date())
                .imageUrl("http://localhost:8080/imagen2")
                .terminationDate(new Date()).build();

        Person personTeacher = Person.builder()
                .idPerson("th")
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

        Teacher teacher = Teacher.builder()
                .idTeacher("1")
                .comments("comentarios")
                .branch(Branch.BACK)
                .person(personTeacher)
                .students(List.of()).build();

        student = Student.builder()
                .idStudent("1")
                .numHoursWeek(20)
                .comments("comentarios")
                .branch(Branch.BACK)
                .person(personStudent)
                .teacher(teacher)
                .subjects(new HashSet<>()).build();
    }

    @DisplayName("Test for the findByStudentId method")
    @Test
    void whenFindByStudentId_returnOptionalOfSubjectListOutputDto() {
        //When
        //Caso1: Cuando no encuentra el estudiante por su id
        Mockito.when(studentRepository.findById("1")).thenReturn(Optional.empty());

        //Caso bueno
        Mockito.when(studentRepository.findById("2")).thenReturn(Optional.of(student));

        //Then
        //Caso cuando el parámetro de entrada es null
        assertThrows(UnprocessableEntityException.class, ()-> subjectService.findByStudentId(null));

        //Caso1
        assertThrows(EntityNotFoundException.class, ()-> subjectService.findByStudentId("1"));
        verify(studentRepository, times(1)).findById("1");

        //Caso bueno
        student.getSubjects().add(subject);
        Optional<SubjectListOutputDto> optional = subjectService.findByStudentId("2");

        assertThat(optional, notNullValue());
        assertTrue(optional.isPresent());
        assertThat(optional.get().getSubjects().size(), equalTo(1));
        verify(studentRepository, times(1)).findById("2");
    }

    @DisplayName("Test for the save method")
    @Test
    void whenSave_returnOptionalOfSubjectOutputDto(){
        //When
        //Caso1: Cuando ya existe la asignatura que queremos guardar por su nombre
        Mockito.when(subjectRepository.findSubjectBySubjectName("Angular")).thenReturn(Optional.of(subject));

        //Caso bueno
        Mockito.when(subjectRepository.findSubjectBySubjectName("CSS")).thenReturn(Optional.empty());
        //Mockito.when(subjectRepository.save(new Subject(subjectInput))).thenReturn(subject);
        Subject subjectDb = new Subject(subjectInput);
        ArgumentCaptor<Subject> subjectArgumentCaptor = ArgumentCaptor.forClass(Subject.class);
        doReturn(subjectDb).when(subjectRepository).save(subjectArgumentCaptor.capture());

        //Then
        //Case when input field is null
        assertThrows(UnprocessableEntityException.class, ()-> subjectService.save(null));

        //Case when field subjectName is not valid
        subjectInput.setSubjectName("No valido");
        assertThrows(UnprocessableEntityException.class, ()-> subjectService.save(subjectInput));

        //Case when initialDate is null
        subjectInput.setInitialDate(null);
        assertThrows(UnprocessableEntityException.class, ()-> subjectService.save(subjectInput));

        //Caso1
        subjectInput.setSubjectName("Angular");
        assertThrows(UnprocessableEntityException.class, ()-> subjectService.save(subjectInput));
        verify(subjectRepository, times(1)).findSubjectBySubjectName("Angular");

        //Caso bueno
        subjectInput.setSubjectName("CSS");
        subjectInput.setInitialDate(new Date());
        Optional<SubjectOutputDto> optional = subjectService.save(subjectInput);

        assertThat(optional, notNullValue());
        assertTrue(optional.isPresent());

        verify(subjectRepository, times(1)).findSubjectBySubjectName("CSS");
        verify(subjectRepository, times(1)).save(subjectArgumentCaptor.capture());
    }

    @DisplayName("Test for the update method")
    @Test
    void whenUpdate_returnOptionalOfSubjectOutputDto() {
        //When
        //Caso1: Cuando no encuentra la asignatura por el id
        Mockito.when(subjectRepository.findById("1")).thenReturn(Optional.empty());

        //Caso2: Cuando el valor de subjectName no es válido
        Mockito.when(subjectRepository.findById("2")).thenReturn(Optional.of(subject));

        //Caso3: Si ya existe el SubjectName en la base de datos
        Mockito.when(subjectRepository.findById("3")).thenReturn(Optional.of(subject));
        Mockito.when(subjectRepository.findSubjectBySubjectName("Angular")).thenReturn(Optional.of(subject));

        //Caso bueno
        Mockito.when(subjectRepository.findById("4")).thenReturn(Optional.of(subject));
        Mockito.when(subjectRepository.findSubjectBySubjectName("CSS")).thenReturn(Optional.empty());
        Subject subjectUpdated = subjectService.getStudentSubjectUpdated(subject, subjectInput);
        Mockito.when(subjectRepository.save(subjectUpdated)).thenReturn(subjectUpdated);

        //Then
        //Cuando los parámetros de entrada son null
        assertThrows(UnprocessableEntityException.class, ()-> subjectService.update(null, null));
        assertThrows(UnprocessableEntityException.class, ()-> subjectService.update(null, subjectInput));
        assertThrows(UnprocessableEntityException.class, ()-> subjectService.update("aa", null));

        //Caso1
        assertThrows(UnprocessableEntityException.class, ()-> subjectService.update("1", subjectInput));
        verify(subjectRepository, times(1)).findById("1");

        //Caso2
        subjectInput.setSubjectName("No valida");
        assertThrows(UnprocessableEntityException.class, ()-> subjectService.update("2", subjectInput));
        verify(subjectRepository, times(1)).findById("2");

        //Caso3
        subjectInput.setSubjectName("Angular");
        assertThrows(UnprocessableEntityException.class, ()-> subjectService.update("3", subjectInput));
        verify(subjectRepository, times(1)).findById("3");
        verify(subjectRepository, times(1)).findSubjectBySubjectName("Angular");

        //Caso bueno
        subjectInput.setSubjectName("CSS");
        subject.setSubjectName(SubjectName.CSS);
        Optional<SubjectOutputDto> optional = subjectService.update("4", subjectInput);

        assertThat(optional, notNullValue());
        assertTrue(optional.isPresent());

        verify(subjectRepository, times(1)).findById("4");
        verify(subjectRepository, times(1)).findSubjectBySubjectName("CSS");
        verify(subjectRepository, times(1)).save(subjectUpdated);
    }

    @DisplayName("Test for the getStudentSubjectUpdated method")
    @Test
    void whenGetStudentSubjectUpdated_returnSubject() {
        //Then
        //Cuando los parámetros son null
        assertThrows(UnprocessableEntityException.class, ()-> subjectService.getStudentSubjectUpdated(null, null));
        assertThrows(UnprocessableEntityException.class, ()-> subjectService.getStudentSubjectUpdated(null, subjectInput));
        assertThrows(UnprocessableEntityException.class, ()-> subjectService.getStudentSubjectUpdated(subject, null));

        //Caso1: Cuando getComments es null
        subjectInput.setComments(null);
        Subject subject1 = subjectService.getStudentSubjectUpdated(subject, subjectInput);

        assertThat(subject1, notNullValue());
        assertThat(subject1.getComments(), notNullValue());
        assertThat(subject1.getComments(), equalTo("Comentarios subject input"));

        //Caso2: Cuando getFinishDate es null
        subjectInput.setFinishDate(null);
        Subject subject2 = subjectService.getStudentSubjectUpdated(subject, subjectInput);

        assertThat(subject1, notNullValue());
        assertThat(subject2.getFinishDate(), notNullValue());
    }
}
