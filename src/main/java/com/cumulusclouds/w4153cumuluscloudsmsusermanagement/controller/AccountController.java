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
import java.util.Optional;
import java.util.Date;

import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.repository.AccountRepository;
import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.model.Account;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.repository.BookerRepository;
import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.model.Booker;
import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.repository.MusicianRepository;
import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.model.Musician;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private long jwtExpirationInMs;

  @Autowired
  private BookerRepository bookerRepository;

  @Autowired
  private MusicianRepository musicianRepository;

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

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    Optional<Account> optionalAccount = accountRepository.findByUsername(loginRequest.getUsername());
    
    if (optionalAccount.isEmpty()) {
        return ResponseEntity.status(401).body("{ \"message\": \"Invalid username or password\" }");
    }

    Account account = optionalAccount.get();
    if (!passwordEncoder.matches(loginRequest.getPassword(), account.getPasswordHash())) {
        return ResponseEntity.status(401).body("{ \"message\": \"Invalid username or password\" }");
    }

    // Generate JWT Token
    String token = generateToken(account);
    return ResponseEntity.ok("{ \"token\": \"" + token + "\", \"message\": \"Login successful\" }");
  }

  private String generateToken(Account account) {
    SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    
    return Jwts.builder()
            .setSubject(account.getUserId().toString()) // The subject can be user ID or any identifier
            .claim("username", account.getUsername())
            .claim("role", "ROLE_USER") // Add additional claims if necessary
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs)) // Set token expiration
            .signWith(key, SignatureAlgorithm.HS256) // Use HS256 algorithm with the secret key
            .compact();
  }

  // Data class for login requests
  public static class LoginRequest {
    private String username;
    private String password;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
    // Check if username or email already exists
    if (accountRepository.findByUsername(registerRequest.getUsername()).isPresent() ||
        accountRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
        return ResponseEntity.status(400).body("{ \"message\": \"Username or email already exists\" }");
    }

    // Create new account
    Account newAccount = new Account();
    newAccount.setUsername(registerRequest.getUsername());
    newAccount.setEmail(registerRequest.getEmail());
    newAccount.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));

    // Save account to the repository
    Account savedAccount = accountRepository.save(newAccount);

    // Return success response
    return ResponseEntity.ok("{ \"message\": \"Registration successful\" }");
  }

  // Data class for register requests
  public static class RegisterRequest {
    private String username;
    private String email;
    private String password;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
  }

  @GetMapping("/{id}/bookers")
  public ResponseEntity<List<Booker>> getBookersForAccount(@PathVariable UUID id) {
    Optional<Account> accountOptional = accountRepository.findById(id);

    if (accountOptional.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    List<Booker> bookers = bookerRepository.findByAccount(accountOptional.get());
    return ResponseEntity.ok(bookers);
  }

  @GetMapping("/{id}/musicians")
  public ResponseEntity<List<Musician>> getMusiciansForAccount(@PathVariable UUID id) {
    Optional<Account> accountOptional = accountRepository.findById(id);

    if (accountOptional.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    List<Musician> musicians = musicianRepository.findByAccount(accountOptional.get());
    return ResponseEntity.ok(musicians);
  }
}