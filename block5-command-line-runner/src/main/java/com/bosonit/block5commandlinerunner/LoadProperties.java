package com.bosonit.block5commandlinerunner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadProperties {

    @Value("${greeting}")
    private String greeting;

    @Value("${my.number}")
    private String number;

    @Value("${new.property: new.property no tiene valor}")
    private String newProperty;

    @Bean
    public String greetingValue() {
        return this.greeting;
    }

    @Bean
    public String myNumberValue() {
        return this.number;
    }

    @Bean
    public String getNewPropertyValue() {
        return this.newProperty;
    }
}
