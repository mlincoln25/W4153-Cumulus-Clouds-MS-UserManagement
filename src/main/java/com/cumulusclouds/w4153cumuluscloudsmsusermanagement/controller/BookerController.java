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
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.model.Booker;
import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.repository.BookerRepository;
import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.model.Account;
import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.repository.AccountRepository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookers")
public class BookerController {

  @Autowired
  private BookerRepository bookerRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Operation(
          summary = "Retrieve all bookers",
          description = "Fetches a list of all available bookers from the database."
  )
  @ApiResponse(
          responseCode = "200",
          description = "Successfully retrieved the list of bookers",
          content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Booker.class)
          )
  )
  @GetMapping("/")
  public ResponseEntity<List<EntityModel<Booker>>> getAllBookers() {
    List<Booker> bookers = bookerRepository.findAll();
    List<EntityModel<Booker>> resources = bookers.stream()
        .map((Booker booker) -> EntityModel.of(booker,
            linkTo(methodOn(BookerController.class).getBookerById(booker.getBookerId())).withSelfRel(),
            linkTo(methodOn(BookerController.class).updateBooker(booker.getBookerId(), booker)).withRel("update"),
            linkTo(methodOn(BookerController.class).deleteBooker(booker.getBookerId())).withRel("delete")))
        .toList();
    return ResponseEntity.ok(resources);
  }

  @Operation(
          summary = "Retrieve booker by ID",
          description = "Fetches a booker based on the provided booker ID."
  )
  @ApiResponse(
          responseCode = "200",
          description = "Booker found successfully",
          content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Booker.class)
          )
  )
  @ApiResponse(
          responseCode = "404",
          description = "Booker not found"
  )
  @Parameter(
          description = "ID of the booker to retrieve",
          required = true
  )
  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<Booker>> getBookerById(@PathVariable UUID id) {
    return bookerRepository.findById(id)
      .map(booker -> EntityModel.of(booker,
          linkTo(methodOn(BookerController.class).getBookerById(id)).withSelfRel(),
          linkTo(methodOn(BookerController.class).updateBooker(id, booker)).withRel("update"),
          linkTo(methodOn(BookerController.class).deleteBooker(id)).withRel("delete")))
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @Operation(
          summary = "Create a new booker",
          description = "Creates a new booker with the provided booker details."
  )
  @ApiResponse(
          responseCode = "200",
          description = "Booker created successfully",
          content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Booker.class)
          )
  )
  @ApiResponse(
          responseCode = "400",
          description = "Invalid booker data provided"
  )
  @PostMapping("/")
  public ResponseEntity<EntityModel<Booker>> createBooker(@RequestBody Booker booker, @RequestParam UUID accountId) {
    Optional<Account> accountOptional = accountRepository.findById(accountId);

    if (accountOptional.isEmpty()) {
        return ResponseEntity.badRequest().body(null); // Return error if account is not found
    }

    booker.setAccount(accountOptional.get());
    Booker savedBooker = bookerRepository.save(booker);

    EntityModel<Booker> resource = EntityModel.of(savedBooker,
        linkTo(methodOn(BookerController.class).getBookerById(savedBooker.getBookerId())).withSelfRel(),
        linkTo(methodOn(BookerController.class).getAllBookers()).withRel("bookers"));

    return ResponseEntity.ok(resource);
  }

  @Operation(
          summary = "Update an existing booker",
          description = "Updates the details of an existing booker based on the provided booker ID."
  )
  @ApiResponse(
          responseCode = "200",
          description = "Booker updated successfully",
          content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Booker.class)
          )
  )
  @ApiResponse(
          responseCode = "404",
          description = "Booker not found"
  )
  @Parameter(
          description = "ID of the booker to update",
          required = true
  )
  @PutMapping("/{id}")
  public ResponseEntity<EntityModel<Booker>> updateBooker(@PathVariable UUID id, @RequestBody Booker bookerDetails) {
    return bookerRepository.findById(id).map(booker -> {
      booker.setOrganizationName(bookerDetails.getOrganizationName());
      booker.setPreferredGenres(bookerDetails.getPreferredGenres());
      booker.setEventType(bookerDetails.getEventType());
      booker.setBookingHistory(bookerDetails.getBookingHistory());
      Booker updatedBooker = bookerRepository.save(booker);
      EntityModel<Booker> resource = EntityModel.of(updatedBooker,
          linkTo(methodOn(BookerController.class).getBookerById(updatedBooker.getBookerId())).withSelfRel(),
          linkTo(methodOn(BookerController.class).getAllBookers()).withRel("bookers"));
      return ResponseEntity.ok(resource);
    }).orElse(ResponseEntity.notFound().build());
  }

  @Operation(
          summary = "Delete a booker",
          description = "Deletes the booker with the specified booker ID."
  )
  @ApiResponse(
          responseCode = "204",
          description = "Booker deleted successfully"
  )
  @ApiResponse(
          responseCode = "404",
          description = "Booker not found"
  )
  @Parameter(
          description = "ID of the booker to delete",
          required = true
  )
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBooker(@PathVariable UUID id) {
    if (bookerRepository.existsById(id)) {
      bookerRepository.deleteById(id);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
  
}