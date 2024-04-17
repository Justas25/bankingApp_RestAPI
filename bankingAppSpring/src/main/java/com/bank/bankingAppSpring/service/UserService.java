package com.bank.bankingAppSpring.service;

import com.bank.bankingAppSpring.dto.PersonDTO;
import com.bank.bankingAppSpring.dto.UserDTO;
import com.bank.bankingAppSpring.entity.Person;
import com.bank.bankingAppSpring.entity.Role;
import com.bank.bankingAppSpring.entity.User;
import com.bank.bankingAppSpring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(PersonDTO personDTO, Person person){
        try {
            Optional<User> existingUser = userRepository.findByPerson(person);
            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("A user with ID " + existingUser.get().getId() + " already exists.");
            } else {
                String encryptedPassword = passwordEncoder.encode(personDTO.password());

                User user = new User(personDTO.username(), encryptedPassword, person, Role.USER);
                userRepository.save(user);
            }
        }
        catch (DataAccessException e) {
            throw new RuntimeException("Error creating user: " + e.getMessage(), e);
        }  catch (IllegalArgumentException e){
            throw new RuntimeException("User name " + personDTO.username() + " already exists: " + e.getMessage(), e);
        }  catch (Exception e) {
            throw new RuntimeException("Unexpected error creating user: " + e.getMessage(), e);
        }
    }

    public Optional<User> getUserByPerson(Person person) {
        try {
            return userRepository.findByPerson(person);
        }  catch (NoSuchElementException e) {
            throw e;
        }  catch (DataAccessException e) {
            throw new RuntimeException("Error accessing user data: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error getting user by person: " + e.getMessage(), e);
        }
    }

    public Optional<User> getUserByUsername(String username) {
       return userRepository.findByUsername(username);
    }

    public void deleteUserById(Long id) {
        try {
            userRepository.deleteById(id);
        }  catch (NoSuchElementException e) {
            throw e;
        }  catch (DataAccessException e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error deleting user: " + e.getMessage(), e);
        }

    }





}
