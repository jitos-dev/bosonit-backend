package com.bosonit.garciajuanjo.block7crudvalidation.repositories;

import com.bosonit.garciajuanjo.block7crudvalidation.models.Person;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.PersonInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.PersonOutputDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.bosonit.garciajuanjo.block7crudvalidation.utils.Constants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Esta clase nos va a servir para realizar los test en el repositorio de PersonRepository. La anotamos con @DataJapTest
 * porque lo que vamos a probar es un repositorio. Para los repositorios utilizamos BDD que se centra en el comportamiento
 * para el usuario final a diferencia de TDD que se centra en la funcionalidad.
 *
 * @author Juan José García
 */
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = PersonRepository.class))
@ActiveProfiles("test")
class PersonRepositoryTests {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    private static PersonInputDto inputDto;
    private static SimpleDateFormat format;
    private static String date;

    @BeforeAll
    public static void initPersonInputDto() {
        inputDto = PersonInputDto.builder()
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

        format = new SimpleDateFormat(FORMAT_DATE);
        date = "2023-07-12";
    }


    /**
     * Para explicar un poco el patrón BDD lo podemos ver de la siguiente forma:
     * given - condición previa o configuración (precondiciones)
     * when - acción o el comportamiento que vamos a probar o ejecutar
     * then - verificar la salida (resultado esperado)
     * -
     * Y un ejemplo en pseudocodigo:
     * Given - Dado que el usuario no ha introducido ningún dato en el formulario
     * When - cuando hace click en el botón de enviar
     * Then - Se deben mostrar los mensajes de validación apropiados
     */
    @DisplayName("Test save Person")
    @Test
    void save() {
        //given
        //Aquí inicializaríamos el objeto PersonInputDto pero que se inicialice una vez antes de cada metodo

        //when
        Person personSave = personRepository.save(new Person(inputDto));

        //then
        assertThat(personSave, notNullValue()); //That personSave is not null
        assertThat(personSave.getIdPerson(), notNullValue()); //That the personSave id is not null
    }

    @Test
    @DisplayName("Test all Persons")
    void all() {
        //Given
        PersonInputDto personInputDto = PersonInputDto.builder()
                .idPerson(null)
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

        personRepository.save(new Person(personInputDto));
        personRepository.save(new Person(inputDto));

        //when
        List<PersonOutputDto> list = personRepository.findAll()
                .stream()
                .map(Person::personToPersonOutputDto)
                .toList();

        //then
        assertThat(list, notNullValue());
        assertThat(list.size(), greaterThan(1));
    }


    @DisplayName("Test find Person by id")
    @Test
    void byId() {
        //Given
        PersonOutputDto outputDto = personRepository.save(new Person(inputDto)).personToPersonOutputDto();

        //When
        Person personBd = personRepository.findById(outputDto.getIdPerson()).get();

        //Then
        assertThat(personBd, notNullValue());
        assertThat(personBd.getIdPerson(), notNullValue());
    }

    @DisplayName("Test update Person")
    @Test
    void update() {
        //Given
        Person person = personRepository.save(new Person(inputDto));

        //When
        person.setActive(false);
        person.setName(PERSON_NAME);
        person.setCity(PERSON_CITY);
        person.setSurname(PERSON_SURNAME);
        person.setUser(PERSON_USER);
        person.setPassword(PERSON_PASSWORD);
        person.setCompanyEmail(PERSON_COMPANY_EMAIL);
        person.setPersonalEmail(PERSON_PERSONAL_EMAIL);
        person.setImageUrl(PERSON_IMAGE_URL);
        person.setCreatedDate(new Date());
        person.setTerminationDate(new Date());

        PersonOutputDto personUpdate = personRepository.save(person).personToPersonOutputDto();

        //Then
        assertThat(personUpdate, notNullValue());
        assertThat(personUpdate.getActive(), notNullValue());
        assertThat(personUpdate.getActive(), is(false));
        assertThat(personUpdate.getName(), notNullValue());
        assertThat(personUpdate.getName(), equalTo(PERSON_NAME));
        assertThat(personUpdate.getCity(), notNullValue());
        assertThat(personUpdate.getCity(), equalTo(PERSON_CITY));
        assertThat(personUpdate.getSurname(), notNullValue());
        assertThat(personUpdate.getSurname(), equalTo(PERSON_SURNAME));
        assertThat(personUpdate.getUser(), notNullValue());
        assertThat(personUpdate.getUser(), equalTo(PERSON_USER));
        assertThat(personUpdate.getPassword(), notNullValue());
        assertThat(personUpdate.getPassword(), equalTo(PERSON_PASSWORD));
        assertThat(personUpdate.getCompanyEmail(), notNullValue());
        assertThat(personUpdate.getCompanyEmail(), equalTo(PERSON_COMPANY_EMAIL));
        assertThat(personUpdate.getPersonalEmail(), notNullValue());
        assertThat(personUpdate.getPersonalEmail(), equalTo(PERSON_PERSONAL_EMAIL));
        assertThat(personUpdate.getImageUrl(), notNullValue());
        assertThat(personUpdate.getImageUrl(), equalTo(PERSON_IMAGE_URL));
        assertThat(personUpdate.getCreatedDate(), notNullValue());
        assertThat(personUpdate.getTerminationDate(), notNullValue());
    }

    @DisplayName("Test delete Person")
    @Test
    void delete() {
        //Given
        Person person = personRepository.save(new Person(inputDto));

        //When
        personRepository.delete(person);
        Long numberPersons = personRepository.count();

        //Then
        assertThat(numberPersons, notNullValue());
        assertThat(numberPersons, equalTo(0L));
    }

    @DisplayName("Test find Person by user")
    @Test
    void byUser() {
        //Given
        Person person = personRepository.save(new Person(inputDto));

        //When
        List<Person> personByName = personRepository.findByUser(person.getUser());

        //Then
        assertThat(personByName, notNullValue());
        assertThat(personByName.size(), greaterThan(0));
        assertThat(personByName.get(0).getIdPerson(), equalTo(person.getIdPerson()));
    }


    @DisplayName("Test find Persons by distinct parameters")
    @Test
    void findPersonsBy() {
        try {
            //Given
            mockData();

            Map<String, Object> values1 = getValues1();
            Map<String, Object> values2 = getValues2();
            Map<String, Object> values3 = getValues3();
            Map<String, Object> values4 = getValues4();
            Map<String, Object> values5 = getValues5();

            //When
            List<PersonOutputDto> list1 = personRepository.findPersonsBy(values1);
            List<PersonOutputDto> list2 = personRepository.findPersonsBy(values2);
            List<PersonOutputDto> list3 = personRepository.findPersonsBy(values3);
            List<PersonOutputDto> list4 = personRepository.findPersonsBy(values4);
            List<PersonOutputDto> list5 = personRepository.findPersonsBy(values5);

            //Then
            assertThat(list1, notNullValue());
            assertThat(list1.size(), equalTo(1));
            assertThat(list2, notNullValue());
            assertThat(list2.size(), equalTo(1));
            assertThat(list3, notNullValue());
            assertThat(list3.size(), equalTo(5));
            assertThat(list4, notNullValue());
            assertThat(list4.size(), equalTo(10));
            assertThat(list4.get(0).getUser(), equalTo("usuario11"));
            assertThat(list5.size(), equalTo(10));
            assertThat(list5.get(0).getName(), equalTo("Ana"));

        } catch (IOException e) {
            fail("Fail to load mock data in findPersonsBy. Url mock data file: " + URL_MOCK_DATA);
        } catch (ParseException e) {
            fail("Fail to parse String date to Date. Date String: " + date);
        }
    }

    private static Map<String, Object> getValues5() throws ParseException {
        Map<String, Object> values = new HashMap<>();
        values.put(NUMBER_PAGE, 0);
        values.put(PAGE_SIZE, 10);
        values.put(ORDER_BY_NAME, false);
        return values;
    }

    private static Map<String, Object> getValues4() throws ParseException {
        Map<String, Object> values = new HashMap<>();
        values.put(NUMBER_PAGE, 1);
        values.put(PAGE_SIZE, 10);
        return values;
    }

    private static Map<String, Object> getValues3() throws ParseException {
        Map<String, Object> values = new HashMap<>();
        values.put(CREATED_DATE, format.parse(date));
        values.put(GREATER_OR_LESS, LESS);
        values.put(NUMBER_PAGE, 0);
        values.put(PAGE_SIZE, 5);
        return values;
    }

    private static Map<String, Object> getValues2() throws ParseException {
        Map<String, Object> values = new HashMap<>();
        values.put(NAME, "Luis");
        values.put(SURNAME, "González");
        values.put(CREATED_DATE, format.parse(date));
        values.put(GREATER_OR_LESS, GREATER);
        values.put(NUMBER_PAGE, 0);
        values.put(PAGE_SIZE, 5);
        return values;
    }

    private static Map<String, Object> getValues1() throws ParseException {
        Map<String, Object> values = new HashMap<>();
        values.put(NAME, "Luis");
        values.put(SURNAME, "González");
        values.put(CREATED_DATE, format.parse(date));
        values.put(GREATER_OR_LESS, LESS);
        values.put(NUMBER_PAGE, 0);
        values.put(PAGE_SIZE, 5);
        return values;
    }

    private void mockData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File jsonFile = resourceLoader.getResource(URL_MOCK_DATA).getFile();

        List<PersonInputDto> list = Arrays.asList(mapper.readValue(jsonFile, PersonInputDto[].class));
        list.forEach(personInputDto -> personRepository.save(new Person(personInputDto)));
    }
}
