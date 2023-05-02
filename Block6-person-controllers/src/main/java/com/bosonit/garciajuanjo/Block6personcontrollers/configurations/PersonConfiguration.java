package com.bosonit.garciajuanjo.Block6personcontrollers.configurations;

import com.bosonit.garciajuanjo.Block6personcontrollers.entities.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "configuration")
public class PersonConfiguration {

    @Bean
    public Person bean1() {
        return new Person("Bean1", "Jaén", 1);
    }

    @Bean
    public Person bean2() {
        return new Person("Bean2", "Jaén", 1);
    }

    @Bean
    public Person bean3() {
        return new Person("Bean3", "Jaén", 1);
    }
}
