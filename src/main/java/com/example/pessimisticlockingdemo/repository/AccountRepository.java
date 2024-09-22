package com.example.pessimisticlockingdemo.repository;

import com.example.pessimisticlockingdemo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
