package com.bosonit.formacion.block14springsecurity.security.controller;

import com.bosonit.formacion.block14springsecurity.exception.EntityNotFoundException;
import com.bosonit.formacion.block14springsecurity.persona.domain.Persona;
import com.bosonit.formacion.block14springsecurity.persona.repository.PersonaRepository;
import com.bosonit.formacion.block14springsecurity.security.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping
public class LoginController {
    @Autowired
    PersonaRepository personaRepository;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> requestMap) {
        String usuario = requestMap.get("usuario");
        String password = requestMap.get("password");

        Persona persona = personaRepository.findByUsuario(usuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado."));

        if (!persona.getPassword().equals(password)) {
            throw new EntityNotFoundException("Password no válida.");
        }

        String role = Boolean.TRUE.equals(persona.getAdmin()) ? "ROLE_ADMIN" : "ROLE_USER";
        return new ResponseEntity<>(jwtTokenUtil.generateToken(usuario, role), HttpStatus.OK);
    }


}
