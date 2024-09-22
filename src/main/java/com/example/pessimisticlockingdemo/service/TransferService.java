package com.example.pessimisticlockingdemo.service;

import com.example.pessimisticlockingdemo.entity.Account;
import com.example.pessimisticlockingdemo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    @Autowired
    private AccountRepository accountRepository;

    //@Transactional
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        try {
            System.out.println(String.format("step 1: Transfer amount: %s from-to %s:%s ", amount, fromAccountId,
                    toAccountId));
            Account fromAccount = accountRepository.findById(fromAccountId)
                    .orElseThrow(() -> new IllegalArgumentException("From account not found"));
            System.out.println(String.format("step 2: Transfer amount: %s from-to %s:%s, get fromAccount: %s ", amount,
                    fromAccountId, toAccountId, fromAccount));

            Account toAccount = accountRepository.findById(toAccountId)
                    .orElseThrow(() -> new IllegalArgumentException("To account not found"));
            System.out.println(String.format("step 3: Transfer amount: %s from-to %s:%s, get toAccount: %s ", amount,
                    fromAccountId, toAccountId, toAccount));

            if (fromAccount.getBalance().compareTo(amount) < 0) {
                throw new IllegalArgumentException("Insufficient funds");
            }

            // Perform the transfer
            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            toAccount.setBalance(toAccount.getBalance().add(amount));
            // Save accounts
            Account fromAccountSaved = accountRepository.save(fromAccount);
            Account toAccountSaved = accountRepository.save(toAccount);
            System.out.println(String.format("step 4: Transfer amount: %s from-to %s:%s, save fromAccountSaved: %s ", amount,
                    fromAccountId, toAccountId, fromAccountSaved));
            System.out.println(String.format("step 5: Transfer amount: %s from-to %s:%s, save toAccount: %s ", amount,
                    fromAccountId, toAccountId, toAccountSaved));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(String.format("step 6: Transfer amount: %s from-to %s:%s, done ", amount, fromAccountId,
                toAccountId));
    }

    @Transactional
    public void transfer2(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        try {
            System.out.println(String.format("step 1: Transfer amount: %s from-to %s:%s ", amount, fromAccountId,
                    toAccountId));
            Account fromAccount = accountRepository.findById(fromAccountId)
                    .orElseThrow(() -> new IllegalArgumentException("From account not found"));
            System.out.println(String.format("step 2: Transfer amount: %s from-to %s:%s, get fromAccount: %s ", amount,
                    fromAccountId, toAccountId, fromAccount));

            Account toAccount = accountRepository.findById(toAccountId)
                    .orElseThrow(() -> new IllegalArgumentException("To account not found"));
            System.out.println(String.format("step 3: Transfer amount: %s from-to %s:%s, get toAccount: %s ", amount,
                    fromAccountId, toAccountId, fromAccount));

            if (fromAccount.getBalance().compareTo(amount) < 0) {
                throw new IllegalArgumentException("Insufficient funds");
            }
            // Perform the transfer
            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            toAccount.setBalance(toAccount.getBalance().add(amount));
            // Save accounts
            Account fromAccountSaved = accountRepository.save(fromAccount);
            Account toAccountSaved = accountRepository.save(toAccount);
            System.out.println(String.format("step 4: Transfer amount: %s from-to %s:%s, save toAccount: %s ", amount,
                    fromAccountId, toAccountId, fromAccountSaved));
            System.out.println(String.format("step 5: Transfer amount: %s from-to %s:%s, save toAccount: %s ", amount,
                    fromAccountId, toAccountId, toAccountSaved));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(String.format("step 6: Transfer amount: %s from-to %s:%s, done ", amount, fromAccountId,
                toAccountId));
    }
}
