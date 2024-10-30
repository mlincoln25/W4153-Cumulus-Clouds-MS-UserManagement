package com.cumulusclouds.w4153cumuluscloudsmsusermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.repository.AccountRepository;
import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.model.Account;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

  @Autowired
  private AccountRepository accountRepository;

  @GetMapping
  public ResponseEntity<List<Account>> getAllAccounts() {
    return ResponseEntity.ok(accountRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Account> getAccountById(@PathVariable UUID id) {
    return accountRepository.findById(id)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Account> createAccount(@RequestBody Account account) {
    return ResponseEntity.ok(accountRepository.save(account));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Account> updateAccount(@PathVariable UUID id, @RequestBody Account accountDetails) {
    return accountRepository.findById(id).map(account -> {
      account.setUsername(accountDetails.getUsername());
      account.setEmail(accountDetails.getEmail());
      account.setPasswordHash(accountDetails.getPasswordHash());
      return ResponseEntity.ok(accountRepository.save(account));
    }).orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAccount(@PathVariable UUID id) {
    if (accountRepository.existsById(id)) {
      accountRepository.deleteById(id);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
  
}
