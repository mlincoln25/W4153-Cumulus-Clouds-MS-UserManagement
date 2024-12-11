package com.cumulusclouds.w4153cumuluscloudsmsusermanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;
import java.util.NoSuchElementException;

import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.repository.AccountRepository;
import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.model.Account;

@Service
@Transactional
public class AccountService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  public List<Account> getAllAccounts() {
    return accountRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Account getAccountById(UUID id) {
    return accountRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Account with ID " + id + " not found"));
  }

  public Account createAccount(Account account) {
    String hashedPassword = passwordEncoder.encode(account.getPasswordHash());
    account.setPasswordHash(hashedPassword);
    return accountRepository.save(account);
  }

  public Account updateAccount(UUID id, Account updatedAccount) {
    Account existingAccount = getAccountById(id);
    existingAccount.setUsername(updatedAccount.getUsername());
    existingAccount.setEmail(updatedAccount.getEmail());

    if (updatedAccount.getPasswordHash() != null && !updatedAccount.getPasswordHash().isEmpty()) {
        String hashedPassword = passwordEncoder.encode(updatedAccount.getPasswordHash());
        existingAccount.setPasswordHash(hashedPassword);
    }

    return accountRepository.save(existingAccount);
  }

  public void deleteAccount(UUID id) {
    if (!accountRepository.existsById(id)) {
        throw new NoSuchElementException("Account with ID " + id + " not found");
    }
    accountRepository.deleteById(id);
  }

}
