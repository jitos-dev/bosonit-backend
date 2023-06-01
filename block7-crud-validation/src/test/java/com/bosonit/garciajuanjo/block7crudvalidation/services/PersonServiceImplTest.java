package com.bosonit.garciajuanjo.block7crudvalidation.services;

import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.EntityNotFoundException;
import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.UnprocessableEntityException;
import com.bosonit.garciajuanjo.block7crudvalidation.models.*;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.PersonCompleteOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.PersonInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.PersonOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.PersonRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.StudentRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.SubjectRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.TeacherRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.services.impl.PersonServiceImpl;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @InjectMocks
    private PersonServiceImpl personService;
    public static PersonInputDto personInputDto;
    public static PersonInputDto personInputDtoNull;

    @BeforeAll
    public static void initPersonInputDto() {
        personInputDto = new PersonInputDto(
                "aaa",
                "usuario1",
                "123456",
                "juanjo",
                "garcia",
                "juanjose.garcia@bosonit.com",
                "jitos86@gmail.com",
                "Sabiote",
                true,
                new Date(),
                "http://localhost:8080/imagen1",
                new Date());

        personInputDtoNull = new PersonInputDto(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }



    @DisplayName("Test fot the delete method when throw exception")
    @Test
    void whenMethodDeleteThrowException() {
        //When
        Mockito.when(personRepository.findById("1")).thenThrow(EntityNotFoundException.class);

        //Then
        assertThrows(EntityNotFoundException.class, () -> {
            personRepository.findById("1");
        });
    }

    @DisplayName("Test fot the delete method when is teacher throw exception")
    @Test
    void whenMethodDeleteTeacherHasStudentsThrowException() {
        //Give
        Person person = Person.builder().idPerson("1").user("usuario1").password("123456").name("juanjo")
                .surname("garcia").companyEmail("bosonit@bosonit.com").personalEmail("jitos86@gmail.com")
                .city("Sabiote").active(true).createdDate(new Date()).imageUrl("http://localhost:8080/imagen1")
                .terminationDate(new Date()).build();

        Teacher teacher = Teacher.builder().idTeacher("2").comments("comentarios").branch(Branch.FULL_STACK)
                .person(new Person(personInputDto)).students(List.of(new Student())).build();

        //When
        Mockito.when(personRepository.findById("1")).thenReturn(Optional.of(person));
        Mockito.when(studentRepository.findByPersonId(person.getIdPerson())).thenReturn(Optional.empty());
        Mockito.when(teacherRepository.findTeacherFromPersonId(person.getIdPerson())).thenReturn(Optional.of(teacher));

        //Then
        assertThrows(UnprocessableEntityException.class, () -> {
            personService.delete("1");
        });
    }

    @DisplayName("Test for the getAll method without params")
    @Test
    void whenMethodGetAllWithoutParam() {
        //When
        Mockito.when(personRepository.findAll()).thenReturn(List.of(new Person(personInputDto)));
        List<PersonCompleteOutputDto> list = personService.getAll();

        //Then
        assertThat(list, notNullValue());
        assertThat(list.size(), equalTo(1));
        assertThat(list.get(0).getTeacher(), nullValue());
        assertThat(list.get(0).getTeacher(), nullValue());
    }

    @DisplayName("Test for the getPersonCompleteOutputDto when the params are null")
    @Test
    void whenGetPersonCompleteOutputDtoOutputParamsAreNull() {
        //Give
        List<PersonOutputDto> persons = List.of();

        //When
        List<PersonCompleteOutputDto> personCompleteList1 =
                personService.getPersonCompleteOutputDto(null, persons);
        List<PersonCompleteOutputDto> personCompleteList2 =
                personService.getPersonCompleteOutputDto(null, null);
        List<PersonCompleteOutputDto> personCompleteList3 =
                personService.getPersonCompleteOutputDto(OutputType.FULL, null);

        //Then
        assertThat(personCompleteList1, notNullValue());
        assertThat(personCompleteList1.size(), equalTo(0));
        assertThat(personCompleteList2, notNullValue());
        assertThat(personCompleteList2.size(), equalTo(0));
        assertThat(personCompleteList3, notNullValue());
        assertThat(personCompleteList3.size(), equalTo(0));
    }

    @DisplayName("Test for the getPersonCompleteOutputDto when is only Person")
    @Test
    void whenGetPersonCompleteOutputDtoOutputTypeSimpleTest() {
        //Give
        List<PersonOutputDto> persons = List.of(getPersonOutputDto());

        //When
        List<PersonCompleteOutputDto> personCompleteList = personService.getPersonCompleteOutputDto(OutputType.SIMPLE, persons);

        //Then
        assertThat(personCompleteList, notNullValue());
        assertThat(personCompleteList.size(), equalTo(1));
        assertThat(personCompleteList.get(0).getTeacher(), nullValue());
        assertThat(personCompleteList.get(0).getStudent(), nullValue());
    }

    @DisplayName("Test for the getPersonCompleteOutputDto when is Teacher")
    @Test
    void whenGetPersonCompleteOutputDtoOutputTypeFull_whenIsTeacher() {
        //Give
        List<PersonOutputDto> persons = List.of(getPersonOutputDto());

        Person person = Person.builder().idPerson("1").user("usuario1").password("123456").name("juanjo")
                .surname("garcia").companyEmail("bosonit@bosonit.com").personalEmail("jitos86@gmail.com")
                .city("Sabiote").active(true).createdDate(new Date()).imageUrl("http://localhost:8080/imagen1")
                .terminationDate(new Date()).build();

        Teacher teacher = Teacher.builder().idTeacher("2").comments("comentarios").branch(Branch.FULL_STACK)
                .person(person).students(new ArrayList<>()).build();

        List<String> personIds = List.of("1");

        //When
        Mockito.when(teacherRepository.findTeachersByPersonsIds(personIds)).thenReturn(List.of(teacher));
        Mockito.when(studentRepository.findStudentsByPersonsIds(personIds)).thenReturn(new ArrayList<>());
        List<PersonCompleteOutputDto> personCompleteList = personService.getPersonCompleteOutputDto(OutputType.FULL, persons);

        //Then
        assertThat(personCompleteList, notNullValue());
        assertThat(personCompleteList.size(), equalTo(1));
        assertThat(personCompleteList.get(0).getTeacher(), notNullValue());
        assertThat(personCompleteList.get(0).getStudent(), nullValue());
        assertThat(personCompleteList.get(0).getTeacher().getPerson().getIdPerson(),
                equalTo(personCompleteList.get(0).getPerson().getIdPerson()));
    }

    @DisplayName("Test for the getPersonCompleteOutputDto when is Student")
    @Test
    void whenGetPersonCompleteOutputDtoOutputTypeFull_whenIsStudent() {
        //Give
        List<PersonOutputDto> persons = List.of(getPersonOutputDto());

        Person person = Person.builder().idPerson("1").user("usuario1").password("123456").name("juanjo")
                .surname("garcia").companyEmail("bosonit@bosonit.com").personalEmail("jitos86@gmail.com")
                .city("Sabiote").active(true).createdDate(new Date()).imageUrl("http://localhost:8080/imagen1")
                .terminationDate(new Date()).build();

        Teacher teacher = Teacher.builder().idTeacher("2").comments("comentarios").branch(Branch.FULL_STACK)
                .person(new Person(personInputDto)).students(new ArrayList<>()).build();

        Student student = Student.builder().idStudent("aaa").numHoursWeek(20).comments("comentarios").branch(Branch.BACK)
                .person(person).teacher(teacher).subjects(new HashSet<>()).build();

        List<String> personIds = List.of("1");

        //When
        Mockito.when(teacherRepository.findTeachersByPersonsIds(personIds)).thenReturn(new ArrayList<>());
        Mockito.when(studentRepository.findStudentsByPersonsIds(personIds)).thenReturn(List.of(student));
        List<PersonCompleteOutputDto> personCompleteList = personService.getPersonCompleteOutputDto(OutputType.FULL, persons);

        //Then
        assertThat(personCompleteList, notNullValue());
        assertThat(personCompleteList.size(), equalTo(1));
        assertThat(personCompleteList.get(0).getStudent(), notNullValue());
        assertThat(personCompleteList.get(0).getTeacher(), nullValue());
        assertThat(personCompleteList.get(0).getStudent().getPerson().getIdPerson(),
                equalTo(personCompleteList.get(0).getPerson().getIdPerson()));
    }

    private PersonOutputDto getPersonOutputDto() {
        return PersonOutputDto.builder().idPerson("1").user("usuario1").password("123456").name("juanjo")
                .surname("garcia").companyEmail("bosonit@bosonit.com").personalEmail("jitos86@gmail.com")
                .city("Sabiote").active(true).createdDate(new Date()).imageUrl("http://localhost:8080/imagen1")
                .terminationDate(new Date()).build();
    }

    @DisplayName("Test for the isAllFieldsCorrect method")
    @ParameterizedTest(name = "Test #{index}: with PersonInputDto, correct value {1}")
    @MethodSource("argumentsForTestingIsAllFieldsCorrectMethod")
    void methodIsAllFieldsCorrectTest(PersonInputDto inputDto, Boolean correctValue) {
        if (correctValue)
            assertTrue(personService.isAllFieldsCorrect(inputDto));

        else
            Assertions.assertThrows(UnprocessableEntityException.class, () -> {
                personService.isAllFieldsCorrect(inputDto);
            });
    }


    @DisplayName("Test to check that inputs fields are valid in personUpdated method")
    @ParameterizedTest(name = "Test #{index}: PersonInput: {0}, Person: {1}")
    @MethodSource("argumentsForTestingInputsFieldAreValid")
    void checkThatInputsFieldsAreValidInPersonUpdatedTest(PersonInputDto inputDto, Person person) {
        Assertions.assertThrows(UnprocessableEntityException.class, () -> {
            personService.checkInputsAreValid(inputDto, person);
        });

    }


    @DisplayName("Test for the getPersonUpdated method")
    @Test
    void methodGetPersonUpdatedTest() {
        //Given
        PersonInputDto nullInputDto = PersonInputDto.builder()
                .idPerson(null)
                .user("usuario")
                .password(null)
                .name(null)
                .surname(null)
                .companyEmail(null)
                .personalEmail(null)
                .city(null)
                .active(true)
                .createdDate(null)
                .imageUrl(null)
                .terminationDate(null).build();

        //When
        Person personUpdated = personService.getPersonUpdated(nullInputDto, new Person(personInputDto));

        //Then
        assertThat(personUpdated.getName(), notNullValue());
        assertThat(personUpdated.getName(), equalTo("juanjo"));
        assertThat(personUpdated.getUser(), equalTo("usuario1"));
        assertThat(personUpdated.getPassword(), notNullValue());
        assertThat(personUpdated.getPassword(), equalTo("123456"));
        assertThat(personUpdated.getSurname(), notNullValue());
        assertThat(personUpdated.getSurname(), equalTo("garcia"));
        assertThat(personUpdated.getCompanyEmail(), notNullValue());
        assertThat(personUpdated.getCompanyEmail(), equalTo("juanjose.garcia@bosonit.com"));
        assertThat(personUpdated.getPersonalEmail(), notNullValue());
        assertThat(personUpdated.getPersonalEmail(), equalTo("jitos86@gmail.com"));
        assertThat(personUpdated.getCity(), notNullValue());
        assertThat(personUpdated.getCity(), equalTo("Sabiote"));
        assertThat(personUpdated.getActive(), notNullValue());
        assertTrue(personUpdated.getActive());
        assertThat(personUpdated.getCreatedDate(), notNullValue());
        assertThat(personUpdated.getTerminationDate(), notNullValue());
        assertThat(personUpdated.getImageUrl(), notNullValue());
        assertThat(personUpdated.getImageUrl(), equalTo("http://localhost:8080/imagen1"));
    }

    private static List<Arguments> argumentsForTestingInputsFieldAreValid() {
        return List.of(
                Arguments.of(null, null),
                Arguments.of(null, new Person()),
                Arguments.of(personInputDto, null),
                Arguments.of(getInputDtoUserNull(), new Person()),
                Arguments.of(getInputDtoUserLessThanSix(), new Person()),
                Arguments.of(getInputDtoUserMoreThanTen(), new Person())
        );
    }

    private static List<Arguments> argumentsForTestingIsAllFieldsCorrectMethod() {
        return List.of(
                Arguments.of(getInputDtoUserNull(), false),
                Arguments.of(getInputDtoUserLessThanSix(), false),
                Arguments.of(getInputDtoUserMoreThanTen(), false),
                Arguments.of(getInputDtoPasswordNull(), false),
                Arguments.of(getInputDtoCompanyEmailNull(), false),
                Arguments.of(getInputDtoPersonalEmailNull(), false),
                Arguments.of(getInputDtoCityNull(), false),
                Arguments.of(getInputDtoActiveNull(), false),
                Arguments.of(getInputDtoCreateDateNull(), false),
                Arguments.of(personInputDto, true)
        );
    }

    private static PersonInputDto getInputDtoCreateDateNull() {
        return PersonInputDto.builder().idPerson(null).user("usuario2").password("123456")
                .name("Maria")
                .surname("Padilla")
                .companyEmail("maria.garcia@bosonit.com")
                .personalEmail("maria@gmail.com")
                .city("Sabiote")
                .active(true)
                .createdDate(null)
                .imageUrl("http://localhost:8080/imagen2")
                .terminationDate(new Date()).build();
    }

    private static PersonInputDto getInputDtoActiveNull() {
        return PersonInputDto.builder().idPerson(null).user("usuario2").password("123456")
                .name("Maria")
                .surname("Padilla")
                .companyEmail("maria.garcia@bosonit.com")
                .personalEmail("maria@gmail.com")
                .city("Sabiote")
                .active(null)
                .createdDate(new Date())
                .imageUrl("http://localhost:8080/imagen2")
                .terminationDate(new Date()).build();
    }

    private static PersonInputDto getInputDtoCityNull() {
        return PersonInputDto.builder().idPerson(null).user("usuario2").password("123456")
                .name("Maria")
                .surname("Padilla")
                .companyEmail("maria.garcia@bosonit.com")
                .personalEmail("maria@gmail.com")
                .city(null)
                .active(true)
                .createdDate(new Date())
                .imageUrl("http://localhost:8080/imagen2")
                .terminationDate(new Date()).build();
    }

    private static PersonInputDto getInputDtoPersonalEmailNull() {
        return PersonInputDto.builder().idPerson(null).user("usuario2").password("123456")
                .name("Maria")
                .surname("Padilla")
                .companyEmail("maria.garcia@bosonit.com")
                .personalEmail(null)
                .city("Sabiote")
                .active(true)
                .createdDate(new Date())
                .imageUrl("http://localhost:8080/imagen2")
                .terminationDate(new Date()).build();
    }

    private static PersonInputDto getInputDtoCompanyEmailNull() {
        return PersonInputDto.builder().idPerson(null).user("usuario2").password("123456")
                .name("Maria")
                .surname("Padilla")
                .companyEmail(null)
                .personalEmail("maria@gmail.com")
                .city("Sabiote")
                .active(true)
                .createdDate(new Date())
                .imageUrl("http://localhost:8080/imagen2")
                .terminationDate(new Date()).build();
    }

    private static PersonInputDto getInputDtoPasswordNull() {
        return PersonInputDto.builder().idPerson(null).user("usuario2").password(null)
                .name("Maria")
                .surname("Padilla")
                .companyEmail("maria.garcia@bosonit.com")
                .personalEmail("maria@gmail.com")
                .city("Sabiote")
                .active(true)
                .createdDate(new Date())
                .imageUrl("http://localhost:8080/imagen2")
                .terminationDate(new Date()).build();
    }

    private static PersonInputDto getInputDtoUserMoreThanTen() {
        return PersonInputDto.builder().idPerson(null).user("Francisco Javier").password("123456")
                .name("Maria")
                .surname("Padilla")
                .companyEmail("maria.garcia@bosonit.com")
                .personalEmail("maria@gmail.com")
                .city("Sabiote")
                .active(true)
                .createdDate(new Date())
                .imageUrl("http://localhost:8080/imagen2")
                .terminationDate(new Date()).build();
    }

    private static PersonInputDto getInputDtoUserLessThanSix() {
        return PersonInputDto.builder().idPerson(null).user("Paco").password("123456")
                .name("Maria")
                .surname("Padilla")
                .companyEmail("maria.garcia@bosonit.com")
                .personalEmail("maria@gmail.com")
                .city("Sabiote")
                .active(true)
                .createdDate(new Date())
                .imageUrl("http://localhost:8080/imagen2")
                .terminationDate(new Date()).build();
    }

    private static PersonInputDto getInputDtoUserNull() {
        return PersonInputDto.builder().idPerson(null).user(null).password("123456")
                .name("Maria")
                .surname("Padilla")
                .companyEmail("maria.garcia@bosonit.com")
                .personalEmail("maria@gmail.com")
                .city("Sabiote")
                .active(true)
                .createdDate(new Date())
                .imageUrl("http://localhost:8080/imagen2")
                .terminationDate(new Date()).build();
    }
}
