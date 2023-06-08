package com.bosonit.garciajuanjo.block7crudvalidation.services;

import com.bosonit.garciajuanjo.block7crudvalidation.models.auth.AuthenticationRequest;
import com.bosonit.garciajuanjo.block7crudvalidation.models.auth.AuthenticationResponse;
import com.bosonit.garciajuanjo.block7crudvalidation.models.auth.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse login(AuthenticationRequest request);
}

