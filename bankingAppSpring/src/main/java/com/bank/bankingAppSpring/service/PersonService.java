package com.bank.bankingAppSpring.service;

import com.bank.bankingAppSpring.dto.PersonDTO;
import com.bank.bankingAppSpring.entity.Person;
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
    @Autowired
    private final PersonRepository personRepository;

    // Constructor injection for PersonRepository with the personRepository bean
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Create a new person.
     *
     * @param personDTO The data transfer object containing the details of the person to be created.
     * @return The newly created Person object.
     */
    public Person createPerson(PersonDTO personDTO) {
        try {
            Person person = new Person();
            person.setId(personDTO.id());
            person.setFirstName(personDTO.firstName());
            person.setSecondName(personDTO.secondName());
            return personRepository.save(person);
        } catch (DataAccessException e) {
            // Handle data access exceptions (e.g., database connectivity issues, SQL syntax errors)
            throw new RuntimeException("Error writing person data: " + e.getMessage(), e);
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
                PersonDTO personDTO = new PersonDTO(person.getId(), person.getFirstName(), person.getSecondName());
                personDtoList.add(personDTO);
            }
            return personDtoList;
        } catch (DataAccessException e) {
            // Handle data access exceptions (e.g., database connectivity issues, query execution errors)
            throw new RuntimeException("Error accessing persons data: " + e.getMessage(), e);
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            throw new RuntimeException("Unexpected error getting persons: " + e.getMessage(), e);
        }
    }

    /**
     * Update an existing person.
     * Note: persons Id cannot be updated, only firstName and secondName personDto attributes are used
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
                return new PersonDTO(existingPerson.getId(), existingPerson.getFirstName(), existingPerson.getSecondName());
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
     * Delete a person by ID.
     *
     * @param id The ID of the person to be deleted.
     */
    public void deletePerson(String id){
        try {
            personRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Person with id "+ id + " not found."));
            personRepository.deleteById(id);
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
