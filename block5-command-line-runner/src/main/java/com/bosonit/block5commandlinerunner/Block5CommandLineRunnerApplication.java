package com.bosonit.block5commandlinerunner;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
@Slf4j
public class Block5CommandLineRunnerApplication {

	public static void main(String[] args) {

		SpringApplication.run(Block5CommandLineRunnerApplication.class, args);
	}

	/*Para este ejemplo, cuando se ejecuta la aplicación, la primera función que se ejecuta es
	* firstFunction() ya que está anotada con @PostConstruct y eso lo que hace es ejecutar el código
	* cuando el bean ha sido creado y configurado pero antes de que este en el contenedor de beans de
	* la aplicación mientras que las funciones dentro de CommandLineRunner se ejecutan justo después
	* de la última linea de inicialización de la aplicación porque en ese punto ya estaría el bean
	* dentro del contenedor.
	*
	* @PostConstruct se suele utilizar para inicializar configuraciones que solo deben realizarse
	* una vez y que no dependan de otros beans ya que no los va a encontrar en el contenedor mientras
	* que CommandLineRunner se suele utilizar cuando necesitamos que este completamente lista la app
	* antes de ejecutar el código como por ejemplo comprobaciones de configuración o actualizaciones
	* de la base de datos.
	* @PostConstruct se ejecuta una vez después de la construcción del bean mientras que
	* CommandLineRunner se ejecuta una vez durante la inicialización de la aplicación*/

	@PostConstruct
	private void firstFunction() {
		System.out.println("Hola desde clase inicial");
	}

	private void secondFunction() {
		System.out.println("Hola desde clase secundaria");
	}

	private void thirdFunction() {
		System.out.println("Soy la tercera clase");
	}

	@Bean
	CommandLineRunner commandLineRunner(String... args) {
		return value -> {
			log.info("Value: " + Arrays.asList(value));
			secondFunction();
			thirdFunction();
		};
	}
}
