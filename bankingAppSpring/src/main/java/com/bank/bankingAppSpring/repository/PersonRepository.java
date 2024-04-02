package com.bank.bankingAppSpring.repository;

import com.bank.bankingAppSpring.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person,String> {

}
