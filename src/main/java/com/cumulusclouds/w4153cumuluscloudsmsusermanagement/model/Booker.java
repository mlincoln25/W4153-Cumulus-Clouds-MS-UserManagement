package com.cumulusclouds.w4153cumuluscloudsmsusermanagement.model;

import java.util.UUID;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "bookers")
public class Booker {

  @Id
  @GeneratedValue
  @Column(name = "booker_id", updatable = false, nullable = false)
  private UUID bookerId;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private Account account;

  @Column(name = "organization_name", length = 100)
  private String organizationName;

  @Column(name = "preferred_genres")
  private List<String> preferredGenres;

  @Column(name = "event_type", length = 100)
  private String eventType;

  @Column(name = "booking_history", columnDefinition = "TEXT")
  private String bookingHistory;

  public UUID getBookerId() {
    return bookerId;
  }

  public void setBookerId(UUID bookerId) {
    this.bookerId = bookerId;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public String getOrganizationName() {
    return organizationName;
  }

  public void setOrganizationName(String organizationName) {
    this.organizationName = organizationName;
  }

  public List<String> getPreferredGenres() {
    return preferredGenres;
  }

  public void setPreferredGenres(List<String> preferredGenres) {
    this.preferredGenres = preferredGenres;
  }

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public String getBookingHistory() {
    return bookingHistory;
  }

  public void setBookingHistory(String bookingHistory) {
    this.bookingHistory = bookingHistory;
  }
}
