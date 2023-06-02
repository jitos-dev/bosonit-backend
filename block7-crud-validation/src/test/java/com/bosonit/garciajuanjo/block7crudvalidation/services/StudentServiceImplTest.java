package com.bosonit.garciajuanjo.block7crudvalidation.services;

import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.EntityNotFoundException;
import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.UnprocessableEntityException;
import com.bosonit.garciajuanjo.block7crudvalidation.models.*;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.PersonCompleteOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.StudentInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.StudentOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.PersonRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.StudentRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.SubjectRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.TeacherRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.services.impl.StudentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private static Student student;
    private static Person personStudent;
    private static Teacher teacher;

    @BeforeAll
    static void initValues() {
        personStudent = Person.builder()
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

        teacher = Teacher.builder()
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

    @DisplayName("Test for the getStudentUpdated with null params")
    @ParameterizedTest(name = "Test #{index}: StudentInputDto: {0}, Student: {1}")
    @MethodSource("argumentsForGetStudentUpdatedNull")
    void whenGetStudentUpdatedWithParamsAreNull(StudentInputDto inputDto, Student student) {
        assertThrows(UnprocessableEntityException.class, () ->
                studentService.getStudentUpdated(inputDto, student));
    }

    @DisplayName("Test for the checkIsAllFieldsCorrect with null params")
    @ParameterizedTest(name = "Test #{index}: StudentInputDto: {0}")
    @MethodSource("argumentsForCheckIsAllFieldsCorrect")
    void whenCheckIsAllFieldsCorrectThrowException(StudentInputDto inputDto) {
        assertThrows(UnprocessableEntityException.class, () ->
                studentService.checkIsAllFieldsCorrect(inputDto));
    }

    @DisplayName("Test for the getStudentUpdated with valid params")
    @Test
    void whenGetStudentUpdated_returnValidStudent() {
        //Give
        StudentInputDto studentInputDtoNull = StudentInputDto.builder()
                .personId("1")
                .branch(null)
                .comments(null)
                .numHoursWeek(null)
                .teacherId("1").build();

        //When
        Student studentUpdated = studentService.getStudentUpdated(studentInputDtoNull, student);

        //Then
        assertThat(studentUpdated, notNullValue());
        assertThat(studentUpdated.getBranch(), notNullValue());
        assertThat(studentUpdated.getBranch(), equalTo(Branch.BACK));
        assertThat(studentUpdated.getComments(), notNullValue());
        assertThat(studentUpdated.getComments(), equalTo("comentarios"));
        assertThat(studentUpdated.getNumHoursWeek(), notNullValue());
        assertThat(studentUpdated.getNumHoursWeek(), equalTo(20));
    }

    @DisplayName("Test for the getById method")
    @Test
    void whenGetById_returnOptionalOfPersonCompleteOutputDto(){
        //Give
        OutputType outputType = OutputType.FULL;
        Person person = Person.builder()
                .idPerson("1")
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
                .terminationDate(new Date())
                .build();

        //When
        //Caso1: Cuando no encuentra un estudiante por el id
        Mockito.when(studentRepository.findById("1")).thenReturn(Optional.empty());

        //Caso2: Cuando outputType es FULL y no encuentra la persona
        Mockito.when(studentRepository.findById("2")).thenReturn(Optional.of(student));
        Mockito.when(personRepository.findById(student.getPerson().getIdPerson())).thenReturn(Optional.empty());

        //Caso bueno
        Mockito.when(studentRepository.findById("3")).thenReturn(Optional.of(student));
        Mockito.when(personRepository.findById("3")).thenReturn(Optional.of(person));

        //Then
        //Caso null: Cuando los parámetros de entrada son null
        assertThrows(UnprocessableEntityException.class, ()-> studentService.getById(null, null));

        //Caso1
        assertThrows(EntityNotFoundException.class, ()-> studentService.getById("1", outputType));

        //Caso2
        assertThrows(EntityNotFoundException.class, ()-> studentService.getById("2", outputType));

        //Caso bueno
        personStudent.setIdPerson("3");
        Optional<PersonCompleteOutputDto> optional = studentService.getById("3", outputType);

        assertThat(optional, notNullValue());
        assertTrue(optional.isPresent());

        verify(studentRepository, times(1)).findById("3");
        verify(personRepository, times(1)).findById("3");
    }

    @DisplayName("Test for the addSubjects method when throw exception")
    @Test
    void whenAddSubjects_throwException() {
        //Give
        Subject subject = Subject.builder()
                .idSubject("1")
                .comments("comentarios")
                .initialDate(new Date())
                .finishDate(new Date())
                .subjectName(SubjectName.HTML)
                .students(Set.of(student)).build();

        student.getSubjects().add(subject);
        teacher.setStudents(List.of(student));

        //When
        //Caso1: Cuando pase el id 2 retorna vacio para probar el EntityNotFoundException
        when(studentRepository.findById("2")).thenReturn(Optional.empty());

        //Caso2: Cuando pasa el 3 devuelve un Student y en subjectRepository devuelve 0
        when(studentRepository.findById("3")).thenReturn(Optional.of(student));
        when(subjectRepository.countExistingSubjectsByIds(List.of("3"))).thenReturn(0L);

        //El caso bueno cuando introduce el id 4
        when(studentRepository.findById("4")).thenReturn(Optional.of(student));
        when(subjectRepository.countExistingSubjectsByIds(List.of("4"))).thenReturn(1L);
        when(subjectRepository.findAllByIds(List.of("4"))).thenReturn(List.of(subject));
        when(studentRepository.save(student)).thenReturn((student));

        //Then
        //Caso1
        List<String> list2 = List.of("2");
        assertThrows(EntityNotFoundException.class, () -> studentService.addSubjects(list2, "2"));

        //Caso2
        List<String> list3 = List.of("3");
        assertThrows(UnprocessableEntityException.class, () -> studentService.addSubjects(list3, "3"));

        //Caso bueno
        Optional<StudentOutputDto> optionalStudent = studentService.addSubjects(List.of("4"), "4");

        assertThat(optionalStudent, notNullValue());
        assertTrue(optionalStudent.isPresent());

        verify(studentRepository, times(1)).findById(("4"));
        verify(subjectRepository, times(1)).countExistingSubjectsByIds(List.of("4"));
        verify(subjectRepository, times(1)).findAllByIds(List.of("4"));
        verify(studentRepository, times(1)).save(student);
    }

    @DisplayName("Test for the save method")
    @Test
    void whenSaveStudent_returnStudentOutputDto() {
        //Given
        StudentInputDto studentInputDto = StudentInputDto.builder()
                .numHoursWeek(20)
                .comments("comentarios")
                .branch(Branch.BACK)
                .personId("1")
                .teacherId("1").build();

        Person person = Person.builder()
                .idPerson("2")
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
                .terminationDate(new Date())
                .build();

        //When
        //Caso1: personRepository.findById salta la excepción
        Mockito.when(personRepository.findById("1")).thenReturn(Optional.empty());

        //Caso2: studentRepository.findStudentIdByPersonId salta la excepción
        Mockito.when(personRepository.findById("2")).thenReturn(Optional.ofNullable(person));
        Mockito.when(studentRepository.findStudentIdByPersonId("2")).thenReturn(Optional.of("2"));

        //Caso3: teacherRepository.findTeacherIdFromIdPerson salta la excepción
        Mockito.when(personRepository.findById("3")).thenReturn(Optional.ofNullable(person));
        Mockito.when(studentRepository.findStudentIdByPersonId("3")).thenReturn(Optional.empty());
        Mockito.when(teacherRepository.findTeacherIdFromIdPerson("3")).thenReturn(Optional.of("3"));

        //Caso4: teacherRepository.findById salta la excepción
        Mockito.when(personRepository.findById("4")).thenReturn(Optional.ofNullable(person));
        Mockito.when(studentRepository.findStudentIdByPersonId("4")).thenReturn(Optional.empty());
        Mockito.when(teacherRepository.findTeacherIdFromIdPerson("4")).thenReturn(Optional.empty());
        Mockito.when(teacherRepository.findById("4")).thenReturn(Optional.empty());

        //Caso5: Caso bueno
        Mockito.when(personRepository.findById("5")).thenReturn(Optional.ofNullable(person));
        Mockito.when(studentRepository.findStudentIdByPersonId("5")).thenReturn(Optional.empty());
        Mockito.when(teacherRepository.findTeacherIdFromIdPerson("5")).thenReturn(Optional.empty());
        Mockito.when(teacherRepository.findById(studentInputDto.getTeacherId())).thenReturn(Optional.ofNullable(teacher));
        Mockito.when(studentRepository.save(new Student(studentInputDto))).thenReturn(student);

        //Then
        //Caso 1
        assertThrows(UnprocessableEntityException.class, () -> studentService.save(studentInputDto));

        //caso2
        studentInputDto.setPersonId("2");
        assertThrows(UnprocessableEntityException.class, () -> studentService.save(studentInputDto));

        //caso3
        assert person != null;
        person.setIdPerson("3");
        studentInputDto.setPersonId("3");
        Assertions.assertThrows(UnprocessableEntityException.class, () -> studentService.save(studentInputDto));

        //caso4
        person.setIdPerson("4");
        studentInputDto.setPersonId("4");
        studentInputDto.setTeacherId("4");
        assertThrows(UnprocessableEntityException.class, () -> studentService.save(studentInputDto));

        //Caso5: Caso bueno
        person.setIdPerson("5");
        studentInputDto.setPersonId("5");
        studentInputDto.setTeacherId(teacher.getIdTeacher());
        teacher.setStudents(List.of(student));

        Optional<StudentOutputDto> optional = studentService.save(studentInputDto);

        assertThat(optional, notNullValue());
        assertTrue(optional.isPresent());

        verify(personRepository, times(1)).findById(("5"));
        verify(studentRepository, times(1)).findStudentIdByPersonId("5");
        verify(teacherRepository, times(1)).findTeacherIdFromIdPerson("5");
        verify(teacherRepository, times(1)).findById(studentInputDto.getTeacherId());
        verify(studentRepository, times(1)).save(new Student(studentInputDto));
    }

    public static List<Arguments> argumentsForCheckIsAllFieldsCorrect() {
        return List.of(
                Arguments.of((Object) null),
                Arguments.of(getStudentInputDtoBranchNull()),
                Arguments.of(getStudentInputDtoNumHoursWeekNull())
        );
    }

    private static List<Arguments> argumentsForGetStudentUpdatedNull() {
        return List.of(
                Arguments.of(null, null),
                Arguments.of(null, new Student()),
                Arguments.of(new StudentInputDto(), null)
        );
    }

    private static StudentInputDto getStudentInputDtoBranchNull() {
        return StudentInputDto.builder()
                .numHoursWeek(20)
                .comments("comentarios")
                .branch(null)
                .personId("1")
                .teacherId("1").build();
    }

    private static StudentInputDto getStudentInputDtoNumHoursWeekNull() {
        return StudentInputDto.builder()
                .numHoursWeek(null)
                .comments("comentarios")
                .branch(Branch.BACK)
                .personId("1")
                .teacherId("1").build();
    }
}
