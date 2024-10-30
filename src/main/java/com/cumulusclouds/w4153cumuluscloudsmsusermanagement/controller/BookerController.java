package com.cumulusclouds.w4153cumuluscloudsmsusermanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.model.Booker;
import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.repository.BookerRepository;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookers")
public class BookerController {

  @Autowired
  private BookerRepository bookerRepository;

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
  public ResponseEntity<List<Booker>> getAllBookers() {
    return ResponseEntity.ok(bookerRepository.findAll());
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
  public ResponseEntity<Booker> getBookerById(@PathVariable UUID id) {
    return bookerRepository.findById(id)
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
  public ResponseEntity<Booker> createBooker(@RequestBody Booker booker) {
    return ResponseEntity.ok(bookerRepository.save(booker));
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
  public ResponseEntity<Booker> updateBooker(@PathVariable UUID id, @RequestBody Booker bookerDetails) {
    return bookerRepository.findById(id).map(booker -> {
      booker.setOrganizationName(bookerDetails.getOrganizationName());
      booker.setPreferredGenres(bookerDetails.getPreferredGenres());
      booker.setEventType(bookerDetails.getEventType());
      booker.setBookingHistory(bookerDetails.getBookingHistory());
      return ResponseEntity.ok(bookerRepository.save(booker));
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