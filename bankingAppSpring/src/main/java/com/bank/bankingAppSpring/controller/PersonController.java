package com.bank.bankingAppSpring.controller;

import com.bank.bankingAppSpring.dto.PersonDTO;
import com.bank.bankingAppSpring.entity.Person;
import com.bank.bankingAppSpring.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path="api/v1/person")
public class PersonController {
    private final PersonService personService;

    //constructor injection of PersonController with the personService bean
    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Create a new person.
     *
     * @param personDTO The data transfer object containing the details of the person to be created.
     * @return ResponseEntity containing the newly created Person object.
     */
    @PostMapping
    public ResponseEntity<?> createPerson(@RequestBody PersonDTO personDTO) {
        try {
            Person newPerson = personService.createPerson(personDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("person", newPerson));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error creating a Person " + e.getMessage()));
        }

    }

    /**
     * Retrieve all persons.
     *
     * @return ResponseEntity containing the list of PersonDTO objects representing all the persons in the repository.
     */
    @GetMapping
    public ResponseEntity<?> getPersons(){
        try {
            if (personService.getPersons().isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok().body(Map.of("data",personService.getPersons()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error getting Person list " + e.getMessage()));

        }


    }

    /**
     * Update an existing person.
     *
     * @param id The ID of the person to be updated.
     * @param updatedPersonDTO The data transfer object containing the updated details of the person.
     * @return ResponseEntity containing the updated PersonDTO object.
     */
    @PutMapping(path = "{id}")
    public ResponseEntity<?> updatePerson(@PathVariable String id,@RequestBody PersonDTO updatedPersonDTO){
        try {
            //return new updatedPersonDto(from the repository)
            PersonDTO updatedPersonDto = personService.updatePerson(id,updatedPersonDTO);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("person", updatedPersonDto));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Bad Arguments " + e.getMessage()));
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Person not found: "+e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error Updating Person: " + e.getMessage()));
        }

    }

    /**
     * Delete a person by ID.
     *
     * @param id The ID of the person to be deleted.
     * @return ResponseEntity indicating the success of the delete operation.
     */
    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> deletePerson(@PathVariable String id) {
        try {
            personService.deletePerson(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }  catch (NoSuchElementException e) {
            return new ResponseEntity<>("Person not found: "+e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred while processing the request: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
