package com.cumulusclouds.w4153cumuluscloudsmsusermanagement.controller;

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

  @GetMapping
  public ResponseEntity<List<Booker>> getAllBookers() {
    return ResponseEntity.ok(bookerRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Booker> getBookerById(@PathVariable UUID id) {
    return bookerRepository.findById(id)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Booker> createBooker(@RequestBody Booker booker) {
    return ResponseEntity.ok(bookerRepository.save(booker));
  }

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