package com.bosonit.garciajuanjo.block7crudvalidation.services.impl;

import com.bosonit.garciajuanjo.block7crudvalidation.config.JwtService;
import com.bosonit.garciajuanjo.block7crudvalidation.exceptions.EntityNotFoundException;
import com.bosonit.garciajuanjo.block7crudvalidation.models.Person;
import com.bosonit.garciajuanjo.block7crudvalidation.models.Role;
import com.bosonit.garciajuanjo.block7crudvalidation.models.auth.AuthenticationRequest;
import com.bosonit.garciajuanjo.block7crudvalidation.models.auth.AuthenticationResponse;
import com.bosonit.garciajuanjo.block7crudvalidation.models.auth.RegisterRequest;
import com.bosonit.garciajuanjo.block7crudvalidation.repositories.PersonRepository;
import com.bosonit.garciajuanjo.block7crudvalidation.services.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager; //administrador de la parte de autenticación

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        //Comprobamos que el usuario no existe ya en la bbdd
        Optional<Person> optional = personRepository.findByUser(request.getUser());
        if (optional.isPresent())
            throw new DataIntegrityViolationException("The user already exist in the database");

        Person person = Person.builder()
                .user(request.getUser())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .surname(request.getSurname())
                .companyEmail(request.getCompanyEmail())
                .personalEmail(request.getPersonalEmail())
                .city(request.getCity())
                .active(true)
                .createdDate(new Date())
                .terminationDate(new Date())
                .imageUrl("http://localhost:8080/image/" + request.getUser())
                .role(Role.USER)
                .build();

        //Guardamos el Person en la base de datos
        personRepository.save(person);

        //Generamos el token con los datos del usuario
        var jwtToken = jwtService.generateToken(person);

        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        //Si no lo encuentra lanza una excepción de tipo AuthenticationException
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUser(), request.getPassword())
        );

        Person person = personRepository.findByUser(request.getUser())
                .orElseThrow(EntityNotFoundException::new);

        //Generamos el token con los datos del usuario
        var jwtToken = jwtService.generateToken(person);

        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();

    }
}
