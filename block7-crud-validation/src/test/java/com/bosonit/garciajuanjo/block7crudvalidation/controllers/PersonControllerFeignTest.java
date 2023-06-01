package com.bosonit.garciajuanjo.block7crudvalidation.controllers;

import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.TeacherOutputDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("server")
class PersonControllerFeignTest {

    @LocalServerPort
    int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @DisplayName("Test when status is OK")
    @Test
    void whenGetTeacherByIdTeacherExist_thenReturnStatusOK() {
        //Give
        String teacherId = "87a8212e-c219-4d7d-bce5-414ace83ffdc";
        String url = "http://localhost:" + port + "/teacher/" + teacherId;

        //When
        RequestEntity<Void> request = RequestEntity.get(url)
                .accept(MediaType.APPLICATION_JSON).build();

        ParameterizedTypeReference<TeacherOutputDto> teacherOutputDto =
                new ParameterizedTypeReference<>() {};

        ResponseEntity<TeacherOutputDto> responseEntity = restTemplate.exchange(request, teacherOutputDto);

        var response = responseEntity.getBody();

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(200));
        assertThat(response, notNullValue());
        assertThat(response.getIdTeacher(), equalTo(teacherId));
    }

    @DisplayName("Test when status is not found")
    @Test
    void whenGetTeacherByIdTeacherThrowExceptions() {
        //Give
        String teacherId = "sin id";
        String url = "http://localhost:" + port + "/teacher/" + teacherId;

        //When
        RequestEntity<Void> request = RequestEntity.get(url)
                .accept(MediaType.APPLICATION_JSON).build();

        ParameterizedTypeReference<TeacherOutputDto> teacherOutputDto =
                new ParameterizedTypeReference<>() {};

        ResponseEntity<TeacherOutputDto> responseEntity = restTemplate.exchange(request, teacherOutputDto);

        var response = responseEntity.getBody();

        //Then
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode().value(), equalTo(404));
        assertThat(response, notNullValue());
        assertThat(response.getIdTeacher(), nullValue());
    }
}

