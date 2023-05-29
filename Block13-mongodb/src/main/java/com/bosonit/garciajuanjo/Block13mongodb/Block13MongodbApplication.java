package com.bosonit.garciajuanjo.Block13mongodb;

import com.bosonit.garciajuanjo.Block13mongodb.models.Person;
import com.bosonit.garciajuanjo.Block13mongodb.models.dtos.PersonInputDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Block13MongodbApplication {

	public static void main(String[] args) {
		SpringApplication.run(Block13MongodbApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(String... args) {
		return value -> {
			//mockData();
		};
	}

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private MongoTemplate mongoTemplate;

	private void mockData() throws IOException {
		Resource resource = resourceLoader.getResource("classpath:mockData.json");
		ObjectMapper objectMapper = new ObjectMapper();
		File jsonFile = resource.getFile();

		List<PersonInputDto> personList = Arrays.asList(objectMapper.readValue(jsonFile, PersonInputDto[].class));
		personList.forEach(inputDto -> {
			mongoTemplate.save(new Person(inputDto));
		});
	}

}
