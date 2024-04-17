package com.bank.bankingAppSpring.authentication;

import com.bank.bankingAppSpring.dto.AuthenticationRequest;
import com.bank.bankingAppSpring.dto.RegisterRequest;
import com.bank.bankingAppSpring.dto.UserDTO;
import com.bank.bankingAppSpring.entity.Role;
import com.bank.bankingAppSpring.entity.User;
import com.bank.bankingAppSpring.jwt.JwtService;
import com.bank.bankingAppSpring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        UserDTO userDTO = UserDTO.fromRegisterRequest(request);

        var user = new User(userDTO.getUsername(),userDTO.getPassword(),
                userDTO.getPerson(),userDTO.getRole() == null || userDTO.getRole().name().isEmpty() ? Role.USER : userDTO.getRole());

        User savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(savedUser);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        UserDTO userDTO = UserDTO.fromAuthenticationRequest(request);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getUsername(),
                        userDTO.getPassword()
                )
        );

        var user = userRepository.findByUsername(userDTO.getUsername())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }
}