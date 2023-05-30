package com.bosonit.garciajuanjo.block7crudvalidation.services;

import com.bosonit.garciajuanjo.block7crudvalidation.models.dto.PersonInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.UnprocessableEntityException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;

public class PersonServiceImplTest {
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

    @Test
    public void isAllFieldsCorrectTest() {
        //assertThat(personInputDto.getUser(), equalTo("usuario1"));
        //assertThat(personInputDto.getUser(), notNullValue());

        Assertions.assertThrows(UnprocessableEntityException.class, () -> {

        });
    }
}
