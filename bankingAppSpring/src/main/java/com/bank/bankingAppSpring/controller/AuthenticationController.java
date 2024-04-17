package com.bank.bankingAppSpring.controller;


import com.bank.bankingAppSpring.authentication.AuthenticationResponse;
import com.bank.bankingAppSpring.authentication.AuthenticationService;
import com.bank.bankingAppSpring.dto.AuthenticationRequest;
import com.bank.bankingAppSpring.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    @Autowired
    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @CrossOrigin(origins = "http://localhost:8080/")
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {
        try {
            AuthenticationResponse response = service.register(request);
            return ResponseEntity.ok(response);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Username already exists"));
        }
    }


    @CrossOrigin(origins = "http://localhost:8080/")
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody AuthenticationRequest request
    ) {
        try{
            AuthenticationResponse response = service.login(request);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Password is incorrect"));
        }
    }
}

