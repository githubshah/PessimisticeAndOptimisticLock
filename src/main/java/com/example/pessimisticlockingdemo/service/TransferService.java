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
            System.out.println(String.format("step:1 getting account"));
            Account account = accountRepository.findById(fromAccountId)
                    .orElseThrow(() -> new IllegalArgumentException("From account not found"));
            System.out.println(String.format("step:2 account fetched: %s", account));
            System.out.println(String.format("step:3 setting balance 5000"));
            account.setBalance(new BigDecimal(5000));
            System.out.println(String.format("step:4 seted balance 5000"));
            // Save accounts
            System.out.println(String.format("step:5 transaction sleep for 3sec"));
            Thread.sleep(3000);
            Account account1 = accountRepository.findById(fromAccountId)
                    .orElseThrow(() -> new IllegalArgumentException("From account not found"));
            System.out.println(String.format("step:6 transaction awake fetched again: %s", account1));
            Account fromaccountsaved = accountRepository.save(account);
            System.out.println(String.format("step:7 after transaction save %s", fromaccountsaved));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void transfer2(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        try {
            Thread.sleep(1000);
            System.err.println(String.format("step:1 getting account"));
            Account account = accountRepository.findById(fromAccountId)
                    .orElseThrow(() -> new IllegalArgumentException("From account not found"));
            System.err.println(String.format("step:2 account fetched: %s", account));
            System.err.println(String.format("step:3 setting balance 80000"));
            account.setBalance(new BigDecimal(80000));
            System.err.println(String.format("step:4 seted balance 80000"));
            // Save accounts
            Account fromaccountsaved = accountRepository.save(account);
            System.err.println(String.format("step:7 after transaction save %s", fromaccountsaved));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
