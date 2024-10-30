package com.cumulusclouds.w4153cumuluscloudsmsusermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.model.Musician;
import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.repository.MusicianRepository;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/musicians")
public class MusicianController {

  @Autowired
  private MusicianRepository musicianRepository;

  @GetMapping
  public ResponseEntity<List<Musician>> getAllMusicians() {
    return ResponseEntity.ok(musicianRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Musician> getMusicianById(@PathVariable UUID id) {
    return musicianRepository.findById(id)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Musician> createMusician(@RequestBody Musician musician) {
    return ResponseEntity.ok(musicianRepository.save(musician));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Musician> updateMusician(@PathVariable UUID id, @RequestBody Musician musicianDetails) {
    return musicianRepository.findById(id).map(musician -> {
      musician.setGenre(musicianDetails.getGenre());
      musician.setInstrumentsPlayed(musicianDetails.getInstrumentsPlayed());
      musician.setYearsOfExperience(musicianDetails.getYearsOfExperience());
      musician.setSampleWorks(musicianDetails.getSampleWorks());
      musician.setAvailability(musicianDetails.getAvailability());
      return ResponseEntity.ok(musicianRepository.save(musician));
    }).orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMusician(@PathVariable UUID id) {
    if (musicianRepository.existsById(id)) {
      musicianRepository.deleteById(id);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
  
}
