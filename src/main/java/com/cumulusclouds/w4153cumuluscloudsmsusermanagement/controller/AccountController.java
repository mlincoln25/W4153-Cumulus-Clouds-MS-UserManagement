package com.cumulusclouds.w4153cumuluscloudsmsusermanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
// import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.UUID;

import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.repository.AccountRepository;
import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.model.Account;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

  @Autowired
  private AccountRepository accountRepository;

  @Operation(
          summary = "Retrieve all accounts",
          description = "Fetches a list of all available accounts from the database."
  )
  @ApiResponse(
          responseCode = "200",
          description = "Successfully retrieved the list of accounts",
          content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Account.class)
          )
  )
  @GetMapping("/")
  public ResponseEntity<List<Account>> getAllAccounts() {
    return ResponseEntity.ok(accountRepository.findAll());
  }

  @Operation(
          summary = "Retrieve account by ID",
          description = "Fetches an account based on the provided account ID."
  )
  @ApiResponse(
          responseCode = "200",
          description = "Account found successfully",
          content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Account.class)
          )
  )
  @ApiResponse(
          responseCode = "404",
          description = "Account not found"
  )
  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<Account>> getAccountById(@PathVariable UUID id) {
    return accountRepository.findById(id)
      .map(account -> {
          EntityModel<Account> resource = EntityModel.of(account);
          resource.add(linkTo(methodOn(AccountController.class).getAccountById(id)).withSelfRel());
          resource.add(linkTo(methodOn(AccountController.class).getAllAccounts()).withRel("accounts"));
          return ResponseEntity.ok(resource);
      })
      .orElse(ResponseEntity.notFound().build());
  }

  @Operation(
          summary = "Create a new account",
          description = "Creates a new account with the provided account details."
  )
  @ApiResponse(
          responseCode = "200",
          description = "Account created successfully",
          content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Account.class)
          )
  )
  @ApiResponse(
          responseCode = "400",
          description = "Invalid account data provided"
  )
  @PostMapping("/")
  public ResponseEntity<EntityModel<Account>> createAccount(@RequestBody Account account) {
    Account savedAccount = accountRepository.save(account);
    EntityModel<Account> resource = EntityModel.of(savedAccount);
    resource.add(linkTo(methodOn(AccountController.class).getAccountById(savedAccount.getUserId())).withSelfRel());
    resource.add(linkTo(methodOn(AccountController.class).getAllAccounts()).withRel("accounts"));
    return ResponseEntity.ok(resource);
  }

  @Operation(
          summary = "Update an existing account",
          description = "Updates the account details for the specified account ID."
  )
  @ApiResponse(
          responseCode = "200",
          description = "Account updated successfully",
          content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Account.class)
          )
  )
  @ApiResponse(
          responseCode = "404",
          description = "Account not found"
  )
  @Parameter(
          description = "ID of the account to update",
          required = true
  )
  @PutMapping("/{id}")
  public ResponseEntity<EntityModel<Account>> updateAccount(@PathVariable UUID id, @RequestBody Account accountDetails) {
    return accountRepository.findById(id).map(account -> {
      account.setUsername(accountDetails.getUsername());
      account.setEmail(accountDetails.getEmail());
      account.setPasswordHash(accountDetails.getPasswordHash());
      return ResponseEntity.ok(EntityModel.of(accountRepository.save(account)));
    }).orElse(ResponseEntity.notFound().build());
  }

  @Operation(
          summary = "Delete an account",
          description = "Deletes the account with the specified account ID."
  )
  @ApiResponse(
          responseCode = "204",
          description = "Account deleted successfully"
  )
  @ApiResponse(
          responseCode = "404",
          description = "Account not found"
  )
  @Parameter(
          description = "ID of the account to delete",
          required = true
  )
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
