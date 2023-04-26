package com.bosonit.juanjose.garcia.Block5logging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class Block5LoggingApplication {

	static Logger logger = LoggerFactory.getLogger(Block5LoggingApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(Block5LoggingApplication.class, args);

		logger.trace("Trace desde main..");
		logger.warn("Warn desde main..");
		logger.error("Error desde main..");
	}

}
