package com.bank.bankingAppSpring.service;

import com.bank.bankingAppSpring.dto.PersonDTO;
import com.bank.bankingAppSpring.entity.Person;
import com.bank.bankingAppSpring.entity.User;
import com.bank.bankingAppSpring.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final UserService userService;

    @Autowired
    public PersonService(PersonRepository personRepository, UserService userService) {
        this.personRepository = personRepository;
        this.userService = userService;
    }

    /**
     * Create a new person.
     *
     * @param personDTO The data transfer object containing the details of the person to be created.
     * @return The newly created Person object.
     */
    public Person createPerson(PersonDTO personDTO) {
        try {
            Optional<Person> existingPersonOptional = personRepository.findById(personDTO.id());
            Optional<User> existingUserOptional = userService.getUserByUsername(personDTO.username());
            if (existingPersonOptional.isPresent()) {
                // If a person with the same ID already exists, handle the situation appropriately
                throw new IllegalArgumentException("A person with ID " + personDTO.id() + " already exists.");
            } else if (existingUserOptional.isPresent()) {
                // If a person with the same ID already exists, handle the situation appropriately
                throw new IllegalArgumentException("A User with username " + personDTO.username() + " already exists.");
            } else {
                Person person = new Person();
                person.setId(personDTO.id());
                person.setFirstName(personDTO.firstName());
                person.setSecondName(personDTO.secondName());
                person = personRepository.save(person);
                //Also create User Entity
                userService.createUser(personDTO, person);
                return person;
            }
        } catch (DataAccessException e) {
            // Handle data access exceptions (e.g., database connectivity issues, SQL syntax errors)
            throw new RuntimeException("Error writing person data: " + e.getMessage(), e);
        }
        catch (IllegalArgumentException e){
            throw e;
        }
        catch (Exception e) {
            // Handle any other unexpected exceptions
            throw new RuntimeException("Unexpected error creating person: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieve all persons.
     *
     * @return A list of PersonDTO objects representing all the persons in the repository.
     */
    public List<PersonDTO> getPersons() {
        try {
            List<Person> personList = personRepository.findAll();
            List<PersonDTO> personDtoList = new ArrayList<>();
            for (Person person: personList) {
                Optional<User> optionalUser= userService.getUserByPerson(person);
                if (optionalUser.isPresent()) {
                    PersonDTO personDTO = new PersonDTO(person.getId(), person.getFirstName(), person.getSecondName(),optionalUser.get().getUsername(),"Password is Hidden");
                    personDtoList.add(personDTO);
                } else {
                    PersonDTO personDTO = new PersonDTO(person.getId(), person.getFirstName(), person.getSecondName(),null,null);
                    personDtoList.add(personDTO);
                }
            }
            return personDtoList;
        } catch (DataAccessException e) {
            // Handle data access exceptions (e.g., database connectivity issues, query execution errors)
            throw new RuntimeException("Error accessing persons data: " + e.getMessage(), e);
        }
        catch (NoSuchElementException e) {
            throw e;
        }
        catch (Exception e) {
            // Handle any other unexpected exceptions
            throw new RuntimeException("Unexpected error getting persons: " + e.getMessage(), e);
        }
    }

    /**
     * Update an existing person.
     * Note: persons Id cannot be updated, only firstName and secondName personDto attributes are used
     * user also cannot be updated here.
     *
     * @param id        The ID of the person to be updated.
     * @param personDTO The data transfer object containing the updated details of the person.
     * @return The updated PersonDTO object.
     */
    public PersonDTO updatePerson(String id, PersonDTO personDTO) {
        try {
            Optional<Person> optionalPerson = personRepository.findById(id);
            if (optionalPerson.isPresent()){
                Person existingPerson = optionalPerson.get();
                existingPerson.setFirstName(personDTO.firstName());
                existingPerson.setSecondName(personDTO.secondName());
                personRepository.save(existingPerson);
                return new PersonDTO(existingPerson.getId(), existingPerson.getFirstName(), existingPerson.getSecondName(), "NOT DISPLAYED", "NOT DISPLAYED");
            } else {
                // Throw NoSuchElementException if the person with the given ID is not found
                throw new NoSuchElementException("Person with id "+ id + " not found.");
            }
        } catch (DataAccessException e) {
            // Handle data access exceptions (e.g., database connectivity issues, query execution errors)
            throw new RuntimeException("Error updating person data: " + e.getMessage(), e);
        } catch (NoSuchElementException e) {
            // Rethrow NoSuchElementException for better handling in the controller
            throw e;
        }
        catch (Exception e) {
            // Handle any other unexpected exceptions
            throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
        }

    }

    /**
     * "Delete" a person by ID. Person cannot be deleted, only user can.
     *
     * @param id The ID of the person to be "deleted".
     */
    public void deletePerson(String id){
        try {
            Person person = personRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Person with id "+ id + " not found."));
            Optional<User> optionalUser = userService.getUserByPerson(person);
            optionalUser.ifPresent(user -> userService.deleteUserById(user.getId()));

        } catch (DataAccessException e) {
            // Handle data access exceptions (e.g., database connectivity issues, query execution errors)
            throw new RuntimeException("Error accessing persons data: " + e.getMessage(), e);
        }
        catch (NoSuchElementException e) {
            // Rethrow NoSuchElementException for better handling in the controller
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while processing the request " + e.getMessage(), e);
        }

    }





}
