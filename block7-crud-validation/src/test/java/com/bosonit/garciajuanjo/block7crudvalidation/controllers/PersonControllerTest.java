package com.bosonit.garciajuanjo.block7crudvalidation.controllers;

import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.EntityNotFoundException;
import com.bosonit.garciajuanjo.block7crudvalidation.models.Person;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.PersonCompleteOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.PersonInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.PersonOutputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.bosonit.garciajuanjo.block7crudvalidation.utils.Constants.URL_MOCK_DATA;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) //para asignarle yo el puerto
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class PersonControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private PersonRepository personRepository;

    private static String url;

    @BeforeAll
    void initValues() throws IOException {
        mockData();
        url = "http://localhost:8080/person";

        log.info("Entra en init values()");
    }

    @Order(1)
    @DisplayName("Test for the allPersons method")
    @Test
    void whenGetAllPersons_returnListOfPersonCompleteOutputDto() {
        //When
        RequestEntity<Void> request = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();
        ParameterizedTypeReference<List<PersonCompleteOutputDto>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<PersonCompleteOutputDto>> responseEntity = restTemplate.exchange(request, responseType);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));
        assertThat(responseEntity.getBody(), notNullValue());
        assertThat(Objects.requireNonNull(responseEntity.getBody()).size(), equalTo(25));
    }

    @Order(2)
    @DisplayName("Test for the getById method")
    @Test
    void whenGetById_returnPersonCompleteOutputDto() {
        //Give
        //Primero obtengo el usuario porque el id es autogenerado de tipo String y no se cual es
        List<Person> list = personRepository.findByUser("usuario25");

        if (list.isEmpty())
            throw new EntityNotFoundException();

        Person person = list.get(0);
        String urlGetById = url + "/" + person.getIdPerson();

        //When
        RequestEntity<Void> request = RequestEntity.get(urlGetById).accept(MediaType.APPLICATION_JSON).build();
        ParameterizedTypeReference<PersonCompleteOutputDto> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<PersonCompleteOutputDto> responseEntity = restTemplate.exchange(request, responseType);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));
        assertThat(responseEntity.getBody(), notNullValue());

        PersonCompleteOutputDto personComplete = responseEntity.getBody();

        assertThat(personComplete.getPerson().getName(), equalTo("Luis"));
        assertThat(personComplete.getPerson().getSurname(), equalTo("González"));
        assertThat(personComplete.getPerson().getCity(), equalTo("Jaén"));
    }

    @Order(3)
    @DisplayName("Test for the personByUser method")
    @Test
    void whenPersonByUser_returnListOfPersonCompleteOutputDto() {
        //Give
        String urlGetByUser = url + "/user/usuario25";

        //When
        RequestEntity<Void> request = RequestEntity.get(urlGetByUser).accept(MediaType.APPLICATION_JSON).build();
        ParameterizedTypeReference<List<PersonCompleteOutputDto>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<PersonCompleteOutputDto>> responseEntity = restTemplate.exchange(request, responseType);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));
        assertThat(responseEntity.getBody(), notNullValue());

        List<PersonCompleteOutputDto> list = responseEntity.getBody();
        PersonCompleteOutputDto personComplete = list.get(0);

        assertThat(list, notNullValue());
        assertThat(list.size(), equalTo(1));
        assertThat(personComplete.getPerson().getName(), equalTo("Luis"));
        assertThat(personComplete.getPerson().getSurname(), equalTo("González"));
        assertThat(personComplete.getPerson().getCity(), equalTo("Jaén"));
    }

    @Order(4)
    @DisplayName("Test for the findPersonsBy method without parameters")
    @Test
    void whenFindPersonsBy_returnListOfPersonOutputDto_withoutParameters() {
        //Give
        String urlFindBy = url + "/findBy/1";

        //When
        RequestEntity<Void> request = RequestEntity.get(urlFindBy).accept(MediaType.APPLICATION_JSON).build();
        ParameterizedTypeReference<List<PersonOutputDto>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<PersonOutputDto>> responseEntity = restTemplate.exchange(request, responseType);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));
        assertThat(responseEntity.getBody(), notNullValue());

        List<PersonOutputDto> list = responseEntity.getBody();
        assertThat(list.size(), equalTo(10));
    }

    @Order(5)
    @DisplayName("Test for the findPersonsBy method without parameters five results per page")
    @Test
    void whenFindPersonsBy_returnListOfPersonOutputDto_withoutParametersFiveResultsPerPage() {
        //Give
        String urlFindBy = url + "/findBy/1?pageSize=5";

        //When
        RequestEntity<Void> request = RequestEntity.get(urlFindBy).accept(MediaType.APPLICATION_JSON).build();
        ParameterizedTypeReference<List<PersonOutputDto>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<PersonOutputDto>> responseEntity = restTemplate.exchange(request, responseType);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));
        assertThat(responseEntity.getBody(), notNullValue());

        List<PersonOutputDto> list = responseEntity.getBody();
        assertThat(list.size(), equalTo(5));
    }

    @Order(6)
    @DisplayName("Test for the findPersonsBy method with the name parameter")
    @Test
    void whenFindPersonsBy_returnListOfPersonOutputDto_withoutWithNameParameter() {
        //Give
        String urlFindBy = url + "/findBy/0?name=Luis";

        //When
        RequestEntity<Void> request = RequestEntity.get(urlFindBy).accept(MediaType.APPLICATION_JSON).build();
        ParameterizedTypeReference<List<PersonOutputDto>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<PersonOutputDto>> responseEntity = restTemplate.exchange(request, responseType);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));
        assertThat(responseEntity.getBody(), notNullValue());

        List<PersonOutputDto> list = responseEntity.getBody();
        assertThat(list.size(), equalTo(2));
    }

    @Order(7)
    @DisplayName("Test for the findPersonsBy method with the name parameter and date less")
    @Test
    void whenFindPersonsBy_returnListOfPersonOutputDto_withoutWithNameParameterAndDateLess() {
        //Give
        String urlFindBy = url + "/findBy/0?name=Luis&createdDate=2023-07-13&greaterOrLess=less";

        //When
        RequestEntity<Void> request = RequestEntity.get(urlFindBy).accept(MediaType.APPLICATION_JSON).build();
        ParameterizedTypeReference<List<PersonOutputDto>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<PersonOutputDto>> responseEntity = restTemplate.exchange(request, responseType);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));
        assertThat(responseEntity.getBody(), notNullValue());

        List<PersonOutputDto> list = responseEntity.getBody();
        PersonOutputDto personOutputDto = list.get(0);

        assertThat(list.size(), equalTo(1));
        assertThat(personOutputDto.getCity(), equalTo("Sevilla"));
    }

    @Order(8)
    @DisplayName("Test for the addPerson method")
    @Test
    void whenAddPerson_returnPersonOutputDto() {
        //Give
        PersonInputDto personInputDto = new PersonInputDto(
                null,
                "inputMock",
                "123456",
                "mock",
                "garcia",
                "mock.garcia@bosonit.com",
                "mock@gmail.com",
                "Sabiote",
                true,
                new Date(),
                "http://localhost:8080/imagenMock",
                new Date());

        //When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PersonInputDto> request = new HttpEntity<>(personInputDto, headers);
        ResponseEntity<PersonOutputDto> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, PersonOutputDto.class);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(201));
        assertThat(responseEntity.getBody(), notNullValue());

        PersonOutputDto personOutputDto = responseEntity.getBody();
        assertThat(personOutputDto.getIdPerson(), notNullValue());
        assertThat(personOutputDto.getName(), equalTo("mock"));
        assertThat(personOutputDto.getUser(), equalTo("inputMock"));
    }

    @Order(9)
    @DisplayName("Test for the update method")
    @Test
    void whenUpdate_returnPersonOutputDto() {
        //Give
        PersonInputDto personInputDto = new PersonInputDto(
                null,
                "updateMock",
                "123456",
                "mockUpdate",
                "garcia",
                "mock.garcia@bosonit.com",
                "mock@gmail.com",
                "Sabiote",
                true,
                new Date(),
                "http://localhost:8080/imagenMock",
                new Date());

        //Primero guardamos la persona para obtener su id
        Person person = personRepository.save(new Person(personInputDto));
        String updateUrl = url + "/" + person.getIdPerson();

        //When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PersonInputDto> request = new HttpEntity<>(personInputDto, headers);
        ResponseEntity<PersonOutputDto> responseEntity = restTemplate.exchange(updateUrl, HttpMethod.PUT, request, PersonOutputDto.class);

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));
        assertThat(responseEntity.getBody(), notNullValue());

        PersonOutputDto personOutputDto = responseEntity.getBody();
        assertThat(personOutputDto.getIdPerson(), notNullValue());
        assertThat(personOutputDto.getName(), equalTo("mockUpdate"));
        assertThat(personOutputDto.getUser(), equalTo("updateMock"));
    }

    @Order(10)
    @DisplayName("Test for the delete method")
    @Test
    void whenDelete_returnPersonOutputDto() {
        //Give
        PersonInputDto personInputDto = new PersonInputDto(
                null,
                "updateMock",
                "123456",
                "mockUpdate",
                "garcia",
                "mock.garcia@bosonit.com",
                "mock@gmail.com",
                "Sabiote",
                true,
                new Date(),
                "http://localhost:8080/imagenMock",
                new Date());

        //Primero guardamos la persona para obtener su id
        Person person = personRepository.save(new Person(personInputDto));
        String deleteUrl = url + "/" + person.getIdPerson();

        //When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PersonInputDto> request = new HttpEntity<>(personInputDto, headers);
        ResponseEntity<PersonOutputDto> responseEntity = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, PersonOutputDto.class);

        Optional<Person> personDelete = personRepository.findById(person.getIdPerson());

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));

        assertFalse(personDelete.isPresent());
    }

    /**
     * Función para cargar los datos de prueba y poder probar el controlador de forma real como si lo hiciéramos con
     * postman
     *
     * @throws IOException si no encuentra la url del archivo JSON con los datos para cargar
     */
    private void mockData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File jsonFile = resourceLoader.getResource(URL_MOCK_DATA).getFile();

        List<PersonInputDto> list = Arrays.asList(mapper.readValue(jsonFile, PersonInputDto[].class));
        list.forEach(personInputDto -> personRepository.save(new Person(personInputDto)));
    }
}
