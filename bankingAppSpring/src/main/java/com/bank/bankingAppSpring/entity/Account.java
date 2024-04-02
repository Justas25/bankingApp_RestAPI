package com.bank.bankingAppSpring.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="Account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String accountType;
    @Column
    private String accountNumber;
    @Column
    private LocalDate openedDate;
    @Column
    private double balance;
    @Column
    private boolean active;
    @ManyToOne
    @JoinColumn(name = "personId")
    private Person person;


    public Account() {
        this.active=true;
    }

    public Account(String accountType, String accountNumber, LocalDate openedDate, double balance, Person person) {
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.openedDate = openedDate;
        this.balance = balance;
        this.active = true;
        this.person = person;
    }

    public Long getId() {
        return id;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public LocalDate getOpenedDate() {
        return openedDate;
    }

    public void setOpenedDate(LocalDate openedDate) {
        this.openedDate = openedDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
