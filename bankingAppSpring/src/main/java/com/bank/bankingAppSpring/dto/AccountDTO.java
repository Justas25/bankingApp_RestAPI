package com.bank.bankingAppSpring.dto;

import com.bank.bankingAppSpring.entity.Person;

import java.time.LocalDate;

public record AccountDTO(
        String accountType,
        String accountNumber,
        LocalDate openedDate,
        double balance,
        boolean active,
        Person person
) {

}