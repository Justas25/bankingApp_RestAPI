package com.bank.bankingAppSpring.dto;

import com.bank.bankingAppSpring.entity.Person;
import com.bank.bankingAppSpring.entity.Role;

public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private Person person;
    private Role role;
    private String accessToken;


    public UserDTO() {
    }

    public UserDTO(Long id, String username, String password, Person person, Role role, String accessToken) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.person = person;
        this.role = role;
        this.accessToken = accessToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public static UserDTO fromRegisterRequest(RegisterRequest request) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(request.getUsername());
        userDTO.setPassword(request.getPassword());
        userDTO.setRole(request.getRole());
        return userDTO;
    }

    public static UserDTO fromAuthenticationRequest(AuthenticationRequest request) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(request.getUsername());
        userDTO.setPassword(request.getPassword());
        return userDTO;
    }





}
