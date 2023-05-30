package com.bosonit.garciajuanjo.block7crudvalidation.services;

import com.bosonit.garciajuanjo.block7crudvalidation.entities.dto.PersonInputDto;
import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.UnprocessableEntityException;
import com.bosonit.garciajuanjo.block7crudvalidation.services.impl.PersonServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Date;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

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
