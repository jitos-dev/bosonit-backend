package com.bosonit.formacion.block14springsecurity.inicio;

import com.bosonit.formacion.block14springsecurity.persona.domain.Persona;
import com.bosonit.formacion.block14springsecurity.persona.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Date;


@Component
public class ComponenteInicio implements CommandLineRunner {
    @Autowired
    PersonaRepository personaRepository;

    private static final String ADMIN = "PersonaAdmin";
    private static final String USER = "PersonaUser";


    @Override
    public void run(String... args) throws Exception {
        Persona personaAdmin = new Persona();

        personaAdmin.setUsuario(ADMIN);
        personaAdmin.setPassword(ADMIN);
        personaAdmin.setName(ADMIN);
        personaAdmin.setSurname(ADMIN);
        personaAdmin.setCompanyEmail(ADMIN + "@example.com");
        personaAdmin.setPersonalEmail(ADMIN + "@gmail.com");
        personaAdmin.setCity("Ciudad" + ADMIN);
        personaAdmin.setActive(true);
        personaAdmin.setImagenUrl("http://example.com/images/" + ADMIN + ".jpg");
        personaAdmin.setCreatedDate(new Date());
        personaAdmin.setAdmin(true);
        personaRepository.save(personaAdmin);

        Persona personaUser = new Persona();

        personaUser.setUsuario(USER);
        personaUser.setPassword(USER);
        personaUser.setName(USER);
        personaUser.setSurname(USER);
        personaUser.setCompanyEmail(USER + "@example.com");
        personaUser.setPersonalEmail(USER + "@gmail.com");
        personaUser.setCity("Ciudad" + USER);
        personaUser.setActive(true);
        personaUser.setImagenUrl("http://example.com/images/" + USER + ".jpg");
        personaUser.setCreatedDate(new Date());
        personaUser.setAdmin(false);
        personaRepository.save(personaUser);

    }
}

