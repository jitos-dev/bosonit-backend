package com.bosonit.garciajuanjo.block7crudvalidation.services.impl;

import com.bosonit.garciajuanjo.block7crudvalidation.models.auth.AuthenticationRequest;
import com.bosonit.garciajuanjo.block7crudvalidation.models.auth.AuthenticationResponse;
import com.bosonit.garciajuanjo.block7crudvalidation.models.auth.RegisterRequest;
import com.bosonit.garciajuanjo.block7crudvalidation.services.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        return null;
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        return null;
    }
}
