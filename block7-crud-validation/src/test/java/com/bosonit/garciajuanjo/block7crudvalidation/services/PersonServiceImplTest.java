package com.bosonit.garciajuanjo.block7crudvalidation.services;

import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.PersonInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.UnprocessableEntityException;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.PersonRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.StudentRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.SubjectRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.TeacherRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.services.impl.PersonServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceImplTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private SubjectRepository subjectRepository;
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

    @DisplayName("Test isAllFieldsCorrect when param is null")
    @ParameterizedTest(name = "Test #{index}: with PersonInputDto, correct value {1}")
    @MethodSource("argumentsForTestingIsAllFieldsCorrectMethod")
    void testWhenPersonInputDtoIsNull(PersonInputDto inputDto, Boolean correctValue) {
        if (correctValue)
            assertTrue(personService.isAllFieldsCorrect(inputDto));

        else
            Assertions.assertThrows(UnprocessableEntityException.class, () -> {
                personService.isAllFieldsCorrect(inputDto);
            });
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
