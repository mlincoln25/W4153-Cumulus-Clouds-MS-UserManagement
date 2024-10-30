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
  
  public UUID getMusicId() {
      return musicId;
  }

  public void setMusicId(UUID musicId) {
      this.musicId = musicId;
  }

  public Account getAccount() {
      return account;
  }

  public void setAccount(Account account) {
      this.account = account;
  }

  public String getGenre() {
      return genre;
  }

  public void setGenre(String genre) {
      this.genre = genre;
  }

  public List<String> getInstrumentsPlayed() {
      return instrumentsPlayed;
  }

  public void setInstrumentsPlayed(List<String> instrumentsPlayed) {
      this.instrumentsPlayed = instrumentsPlayed;
  }

  public int getYearsOfExperience() {
      return yearsOfExperience;
  }

  public void setYearsOfExperience(int yearsOfExperience) {
      this.yearsOfExperience = yearsOfExperience;
  }

  public List<String> getSampleWorks() {
      return sampleWorks;
  }

  public void setSampleWorks(List<String> sampleWorks) {
      this.sampleWorks = sampleWorks;
  }

  public String getAvailability() {
      return availability;
  }

  public void setAvailability(String availability) {
      this.availability = availability;
  }
  
}
