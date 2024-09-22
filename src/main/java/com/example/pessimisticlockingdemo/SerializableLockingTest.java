package com.example.pessimisticlockingdemo;

import com.example.pessimisticlockingdemo.entity.Account;
import com.example.pessimisticlockingdemo.repository.AccountRepository;
import com.example.pessimisticlockingdemo.service.TransferService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;

@Component
public class SerializableLockingTest implements CommandLineRunner {

    @Autowired
    private AccountRepository accountRepository;

    @PostConstruct
    public void run() {
        // Initialize a product
        Account accountA = new Account();
        accountA.setId(1L);
        accountA.setBalance(new BigDecimal(1000));
        accountRepository.save(accountA);

        Account accountB = new Account();
        accountB.setId(2L);
        accountB.setBalance(new BigDecimal(1000));
        accountRepository.save(accountB);

        accountRepository.findAll().forEach(System.out::println);
    }

    @Autowired
    private TransferService transferService;

    @Override
    public void run(String... args) throws Exception {
        extracted();
        Account fromAccount = accountRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("From account not found"));
        System.out.println(String.format("--------final fromAccount: %s ", fromAccount));

        Account toAccount = accountRepository.findById(2L)
                .orElseThrow(() -> new IllegalArgumentException("To account not found"));
        System.out.println(String.format("--------final toAccount: %s ", toAccount));
    }

    private void extracted() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        // Thread 1: Decrease quantity by 5
        new Thread(() -> {
            try {
                System.out.println("Thread 1: Starting transaction to decrease quantity by 5");
                Thread.sleep(100);
                transferService.transfer(1L, 2L, new BigDecimal(100));
                System.out.println("Thread 1: Transaction completed");
            } catch (Exception e) {
                System.err.println("Thread 1: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        }).start();

        // Thread 2: Decrease quantity by 6 (should fail due to insufficient quantity)
        new Thread(() -> {
            try {
                System.out.println("Thread 2: Starting transaction to decrease quantity by 5");
                Thread.sleep(100);
                transferService.transfer2(1L, 2L, new BigDecimal(200));
                System.out.println("Thread 2: Transaction completed");
            } catch (Exception e) {
                System.err.println("Thread 2: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        }).start();

        latch.await();

        Account fromAccount = accountRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("From account not found"));
        System.out.println(String.format("final fromAccount: %s ", fromAccount));

        Account toAccount = accountRepository.findById(2L)
                .orElseThrow(() -> new IllegalArgumentException("To account not found"));
        System.out.println(String.format("final toAccount: %s ", toAccount));
    }
}
