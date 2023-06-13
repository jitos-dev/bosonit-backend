package com.bosonit.garciajuanjo.block7crudvalidation.controllers;

import com.bosonit.garciajuanjo.block7crudvalidation.models.auth.AuthenticationRequest;
import com.bosonit.garciajuanjo.block7crudvalidation.models.auth.AuthenticationResponse;
import com.bosonit.garciajuanjo.block7crudvalidation.models.auth.RegisterRequest;
import com.bosonit.garciajuanjo.block7crudvalidation.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("login")
    public ResponseEntity<AuthenticationResponse> login(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @GetMapping("message")
    public ResponseEntity<String> message () {
        return ResponseEntity.ok("Este es un mensaje de confirmación");
    }

}
