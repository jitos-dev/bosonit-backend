package com.bosonit.juanjosegarcia.Block5profiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Block5ProfilesApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Block5ProfilesApplication.class, args);
	}

	/*Injecto la interface y según el perfil se carga un dato u otro. Como
	* perfil por defecto en application.properties tengo el local pero se puede
	* ejecutar otro cualquiera por medio de consola o en las configuraciones de
	* Intellj Idea y los datos cambian.*/
	@Autowired
	private GenericProfile profile;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Url de la base de datos: " + profile.getDataBaseUrl());
	}
}
