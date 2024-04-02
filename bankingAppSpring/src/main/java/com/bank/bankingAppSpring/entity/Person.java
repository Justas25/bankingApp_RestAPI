package com.bank.bankingAppSpring.entity;

import jakarta.persistence.*;

@Entity
@Table(name="Person")
public class Person {
    @Id
    @Column
    private String id;
    @Column
    private String firstName;
    @Column
    private String secondName;

    public Person() {
    }

    public Person(String id, String firstName, String secondName) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public Person(String firstName, String secondName) {
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }
}
