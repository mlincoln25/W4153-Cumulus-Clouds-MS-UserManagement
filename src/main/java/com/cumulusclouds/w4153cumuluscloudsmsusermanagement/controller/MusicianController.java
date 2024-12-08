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
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.model.Musician;
import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.repository.MusicianRepository;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/musicians")
public class MusicianController {

  @Autowired
  private MusicianRepository musicianRepository;

  @Operation(
          summary = "Retrieve all musicians",
          description = "Fetches a list of all available musicians from the database."
  )
  @ApiResponse(
          responseCode = "200",
          description = "Successfully retrieved the list of musicians",
          content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Musician.class)
          )
  )
  @GetMapping("/")
  public ResponseEntity<List<Musician>> getAllMusicians() {
    return ResponseEntity.ok(musicianRepository.findAll());
  }

  @Operation(
          summary = "Retrieve musician by ID",
          description = "Fetches a musician based on the provided musician ID."
  )
  @ApiResponse(
          responseCode = "200",
          description = "Musician found successfully",
          content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Musician.class)
          )
  )
  @ApiResponse(
          responseCode = "404",
          description = "Musician not found"
  )
  @Parameter(
          description = "ID of the musician to retrieve",
          required = true
  )
  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<Musician>> getMusicianById(@PathVariable UUID id) {
    return musicianRepository.findById(id)
      .map(musician -> {
          EntityModel<Musician> resource = EntityModel.of(musician);
          resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MusicianController.class).getMusicianById(id)).withSelfRel());
          resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MusicianController.class).getAllMusicians()).withRel("all-musicians"));
          return ResponseEntity.ok(resource);
      })
      .orElse(ResponseEntity.notFound().build());
  }

  @Operation(
          summary = "Create a new musician",
          description = "Creates a new musician with the provided musician details."
  )
  @ApiResponse(
          responseCode = "200",
          description = "Musician created successfully",
          content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Musician.class)
          )
  )
  @ApiResponse(
          responseCode = "400",
          description = "Invalid musician data provided"
  )
  @PostMapping("/")
  public ResponseEntity<EntityModel<Musician>> createMusician(@RequestBody Musician musician) {
    Musician savedMusician = musicianRepository.save(musician);
    EntityModel<Musician> resource = EntityModel.of(savedMusician);
    resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MusicianController.class).getMusicianById(savedMusician.getMusicId())).withSelfRel());
    return ResponseEntity.ok(resource);
  }

  @Operation(
          summary = "Update an existing musician",
          description = "Updates the details of an existing musician based on the provided musician ID."
  )
  @ApiResponse(
          responseCode = "200",
          description = "Musician updated successfully",
          content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Musician.class)
          )
  )
  @ApiResponse(
          responseCode = "404",
          description = "Musician not found"
  )
  @Parameter(
          description = "ID of the musician to update",
          required = true
  )
  @PutMapping("/{id}")
  public ResponseEntity<EntityModel<Musician>> updateMusician(@PathVariable UUID id, @RequestBody Musician musicianDetails) {
    return musicianRepository.findById(id).map(musician -> {
      musician.setGenre(musicianDetails.getGenre());
      musician.setInstrumentsPlayed(musicianDetails.getInstrumentsPlayed());
      musician.setYearsOfExperience(musicianDetails.getYearsOfExperience());
      musician.setSampleWorks(musicianDetails.getSampleWorks());
      musician.setAvailability(musicianDetails.getAvailability());
      Musician updatedMusician = musicianRepository.save(musician);
      EntityModel<Musician> resource = EntityModel.of(updatedMusician);
      resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MusicianController.class).getMusicianById(updatedMusician.getMusicId())).withSelfRel());
      return ResponseEntity.ok(resource);
    }).orElse(ResponseEntity.notFound().build());
  }

  @Operation(
          summary = "Delete a musician",
          description = "Deletes the musician with the specified musician ID."
  )
  @ApiResponse(
          responseCode = "204",
          description = "Musician deleted successfully"
  )
  @ApiResponse(
          responseCode = "404",
          description = "Musician not found"
  )
  @Parameter(
          description = "ID of the musician to delete",
          required = true
  )
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
