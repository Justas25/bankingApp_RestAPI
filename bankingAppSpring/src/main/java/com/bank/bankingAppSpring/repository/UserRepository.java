package com.bank.bankingAppSpring.repository;

import com.bank.bankingAppSpring.entity.Account;
import com.bank.bankingAppSpring.entity.Person;
import com.bank.bankingAppSpring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByPerson(Person person);

}
