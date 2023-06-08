package com.bosonit.garciajuanjo.block7crudvalidation.config;

import com.bosonit.garciajuanjo.block7crudvalidation.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final PersonRepository personRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        //Implementamos el método loadUserByUserName con un lambda
        return username -> personRepository.findByUser(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found in ApplicationConfig"));
    }
}
