package com.bank.bankingAppSpring.controller;

import com.bank.bankingAppSpring.dto.AccountDTO;
import com.bank.bankingAppSpring.entity.Account;
import com.bank.bankingAppSpring.entity.Person;
import com.bank.bankingAppSpring.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path="api/v1/account")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Create a new account(USING Form data) based on the provided accountDTO.
     *
     * This method handles the creation of a new account using the provided account DTO.
     * It binds form data to the AccountDTO object and delegates the creation to the accountService.
     *
     * @param accountDTO The accountDTO object containing data for the new account.
     * @return A ResponseEntity representing the status of the account creation operation.
     */
   /* @PostMapping("/form")
    public ResponseEntity<?> createAccountForm(@ModelAttribute AccountDTO accountDTO,@PathVariable("personId") String personId) {
        try {
            Account newAccount = accountService.createNewAccount(accountDTO,personId);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("account", newAccount));
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Error: "+e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error creating a new account "+ e.getMessage()));
        }

    } */

    /**
     * Create a new account(USING JSON object) based on the provided accountDTO.
     *
     * This method handles the creation of a new account using the provided account DTO.
     * It binds form data to the AccountDTO object and delegates the creation to the accountService.
     *
     * @param accountDTO The accountDTO object containing data for the new account.
     * @return A ResponseEntity representing the status of the account creation operation.
     */
    @PostMapping("/{personId}")
    public ResponseEntity<?> createAccount(@PathVariable String personId,@RequestBody AccountDTO accountDTO) {
        try {
            Account newAccount = accountService.createNewAccount(accountDTO,personId);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("account", newAccount));
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Error2: "+e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error creating a new account "+ e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAccounts() {
        try {
            if (accountService.getAccounts().isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok().body(Map.of("data",accountService.getAccounts()));
        } catch (Exception e) {
            //throw new RuntimeException(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error creating a new account " + e.getMessage()));
        }


    }

    // Deposit money into an account
    @PutMapping("/{accountNumber}/deposit")
    public ResponseEntity<?> depositMoney(@PathVariable String accountNumber, @RequestParam double amount) {
        try {
            Account updatedAccount = accountService.depositMoney(accountNumber, amount);
            return ResponseEntity.ok().body(Map.of("message", "Deposit successful", "account", updatedAccount));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Account not found: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid amount: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error: " + e.getMessage()));
        }
    }
    //withdraw money from account
    @PutMapping("/{accountNumber}/withdraw")
    public ResponseEntity<?> withdrawMoney(@PathVariable String accountNumber, @RequestParam double amount) {
        try {
            Account updatedAccount = accountService.withdrawMoney(accountNumber, amount);
            return ResponseEntity.ok().body(Map.of("message", "Deposit successful", "account", updatedAccount));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Account not found: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid amount: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error: " + e.getMessage()));
        }
    }


    // Transfer money between accounts
    @PutMapping("/transfer")
    public ResponseEntity<?> transferMoney(@RequestParam String senderAccountNumber, @RequestParam String receiverAccountNumber, @RequestParam double amount) {
        try {
            accountService.transferMoney(senderAccountNumber, receiverAccountNumber, amount);
            return ResponseEntity.ok().body(Map.of("message", "Transfer successful"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Account not found: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid amount or insufficient balance: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{accountNumber}/suspend")
    public ResponseEntity<?> suspendAccount(@PathVariable String accountNumber) {
        try {
            accountService.suspendAccount(accountNumber);
            return ResponseEntity.ok().body(Map.of("message", "Bank account suspended successfully"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Account not found: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error: " + e.getMessage()));
        }
    }





}


