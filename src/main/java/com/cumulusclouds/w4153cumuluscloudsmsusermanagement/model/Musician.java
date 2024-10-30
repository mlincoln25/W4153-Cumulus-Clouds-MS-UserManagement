package com.cumulusclouds.w4153cumuluscloudsmsusermanagement.model;

import java.util.UUID;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "musician")
public class Musician {
  
  @Id
  @GeneratedValue
  @Column(name = "musician_id", updatable = false, nullable = false)
  private UUID musicId;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private Account account;

  @Column(name = "genre", length = 100)
  private String genre;

  @Column(name = "instruments_played")
  private List<String> instrumentsPlayed;

  @Column(name = "years_of_experience")
  private int yearsOfExperience;

  @Column(name = "sample_works", columnDefinition = "TEXT[]")
  private List<String> sampleWorks;

  @Column(name = "availability", columnDefinition = "TEXT")
  private String availability;
  
}
