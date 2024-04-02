package com.bank.bankingAppSpring.service;

import com.bank.bankingAppSpring.dto.AccountDTO;
import com.bank.bankingAppSpring.entity.Account;
import com.bank.bankingAppSpring.entity.Person;
import com.bank.bankingAppSpring.repository.AccountRepository;
import com.bank.bankingAppSpring.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PersonRepository personRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository,PersonRepository personRepository) {
        this.accountRepository = accountRepository;
        this.personRepository = personRepository;
    }

    @Transactional
    public Account createNewAccount(AccountDTO accountDTO, String personId) {
        try {
            Account newAccount = new Account();
            newAccount.setAccountType(accountDTO.accountType());
            newAccount.setAccountNumber(generateLithuanianIBAN());
            newAccount.setBalance(0.0);
            newAccount.setOpenedDate(LocalDate.now());
            Optional<Person> person = personRepository.findById(personId);
            if (person.isPresent()) {
                newAccount.setPerson(person.get());
                return accountRepository.save(newAccount);
            } else {
                throw new NoSuchElementException("No person with ID: "+personId);
            }
        } catch (NoSuchElementException e) {
            // Rethrow NoSuchElementException for better handling in the controller
            throw e;
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
        }

    }


    //Get active accounts
    public List<AccountDTO> getAccounts() {
        List<Account> accounts = accountRepository.findAll();
        List<AccountDTO> accountsDTO = new ArrayList<>();
        for (Account account: accounts) {
            //if (account.isActive()) {
                AccountDTO accountDTO = new AccountDTO(account.getAccountType(),account.getAccountNumber(),account.getOpenedDate(),account.getBalance(), account.isActive(), account.getPerson());
                accountsDTO.add(accountDTO);
          //  }
        }
        return accountsDTO;
    }



    // Method to generate a random Lithuanian IBAN
    public String generateLithuanianIBAN() {
        // Define country code and bank code
        String countryCode = "LT";
        String bankCode = "1234"; // Replace with actual bank code (BIC)

        // Generate random 11-digit account number
        String accountNumber = generateRandomDigits(11);
        System.out.println(accountNumber);
        // Concatenate country code, bank code, and account number
        String ibanBase = countryCode + "00" + bankCode + accountNumber;
        System.out.println(ibanBase);
        // Calculate checksum using Modulus-97 algorithm
        int checksum = calculateMod97Checksum(ibanBase);
        System.out.println(checksum);
        // Format checksum as two digits (prepend with leading zero if necessary)
        String formattedChecksum = String.format("%02d", checksum);
        System.out.println(formattedChecksum);
        // Concatenate country code, checksum, bank code, and account number to form IBAN
        System.out.println(countryCode + formattedChecksum + bankCode + accountNumber);
        return countryCode + formattedChecksum + bankCode + accountNumber;
    }

    // Helper method to generate random digits of specified length
    private String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // Append random digit (0-9)
        }
        return sb.toString();
    }

    // Helper method to calculate Modulus-97 checksum
    private int calculateMod97Checksum(String ibanBase) {
        BigInteger ibanBigInt = new BigInteger(ibanBase.replaceAll("[^0-9]", ""));
        BigInteger mod97 = ibanBigInt.mod(new BigInteger("97"));
        return 98 - mod97.intValue();
    }

    @Transactional
    public Account depositMoney(String accountNumber, double amount) {
        try {
            Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
            if (optionalAccount.isPresent()) {
                Account account = optionalAccount.get();
                double currentBalance = account.getBalance();
                double newBalance = currentBalance + amount;
                account.setBalance(newBalance);
                return accountRepository.save(account);
            } else {
                throw new NoSuchElementException("No account found with Account Number: " + accountNumber);
            }
        } catch (NoSuchElementException e) {
            // Rethrow NoSuchElementException for better handling in the controller
            throw e;
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Account withdrawMoney(String accountNumber, double amount) {
        try {
            Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
            if (optionalAccount.isPresent()) {
                Account account = optionalAccount.get();
                double currentBalance = account.getBalance();
                double newBalance = currentBalance - amount;
                account.setBalance(newBalance);
                return accountRepository.save(account);
            } else {
                throw new NoSuchElementException("No account found with Account Number: " + accountNumber);
            }
        } catch (NoSuchElementException e) {
            // Rethrow NoSuchElementException for better handling in the controller
            throw e;
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
        }

    }

    @Transactional
    public void transferMoney(String senderAccountNumber, String receiverAccountNumber, double amount) {
        try {
            // Retrieve sender account
            Optional<Account> optionalSenderAccount = accountRepository.findByAccountNumber(senderAccountNumber);
            if (!optionalSenderAccount.isPresent()) {
                throw new NoSuchElementException("No account found with Account Number: " + senderAccountNumber);
            }
            Account senderAccount = optionalSenderAccount.get();

            // Retrieve receiver account
            Optional<Account> optionalReceiverAccount = accountRepository.findByAccountNumberAndActiveIsTrue(receiverAccountNumber);
            if (!optionalReceiverAccount.isPresent()) {
                throw new NoSuchElementException("No account found with Account number: " + receiverAccountNumber);
            }
            Account receiverAccount = optionalReceiverAccount.get();

            // Check if sender has enough balance for the transfer
            double senderBalance = senderAccount.getBalance();
            if (senderBalance < amount) {
                throw new IllegalArgumentException("Insufficient balance in sender account");
            }

            // Perform the transfer
            double senderNewBalance = senderBalance - amount;
            double receiverNewBalance = receiverAccount.getBalance() + amount;
            senderAccount.setBalance(senderNewBalance);
            receiverAccount.setBalance(receiverNewBalance);

            // Save changes to both accounts
            accountRepository.save(senderAccount);
            accountRepository.save(receiverAccount);
        } catch (NoSuchElementException | IllegalArgumentException e) {
            // Rethrow the exceptions for better handling in the controller
            throw e;
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void suspendAccount(String accountNumber) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNumberAndActiveIsTrue(accountNumber);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setActive(false); // Set account status to suspended
            accountRepository.save(account);
        } else {
            throw new NoSuchElementException("Account number "+ accountNumber +" not found or already suspended: ");
        }
    }




}
