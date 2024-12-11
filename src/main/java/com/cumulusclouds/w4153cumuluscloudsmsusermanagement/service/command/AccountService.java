package com.cumulusclouds.w4153cumuluscloudsmsusermanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  public List<Account> getAllAccounts() {
    return accountRepository.findAll();
  }

  public Account getAccountById(UUID id) {
    return accountRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Account not found"));
  }

  public Account createAccount(Account account) {
    return accountRepository.save(account);
  }

  public Account updateAccount(UUID id, Account updatedAccount) {
    Account existingAccount = getAccountById(id);
    existingAccount.setUsername(updatedAccount.getUsername());
    existingAccount.setEmail(updatedAccount.getEmail());
    existingAccount.setPasswordHash(updatedAccount.getPasswordHash());
    return accountRepository.save(existingAccount);
  }

  public void deleteAccount(UUID id) {
    accountRepository.deleteById(id);
  }

}
