package com.cumulusclouds.w4153cumuluscloudsmsusermanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.model.Booker;
import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.repository.BookerRepository;

@Service
@Transactional
public class BookerService {
  @Autowired
  private BookerRepository bookerRepository;

  public List<Booker> getAllBookers() {
    return bookerRepository.findAll();
  }

  public Booker getBookerById(UUID id) {
    return bookerRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Booker not found"));
  }

  public Booker createBooker(Booker booker) {
    return bookerRepository.save(booker);
  }

  public Booker updateBooker(UUID id, Booker updatedBooker) {
    Booker existingBooker = getBookerById(id);
    existingBooker.setOrganizationName(updatedBooker.getOrganizationName());
    existingBooker.setPreferredGenres(updatedBooker.getPreferredGenres());
    existingBooker.setEventType(updatedBooker.getEventType());
    existingBooker.setBookingHistory(updatedBooker.getBookingHistory());
    return bookerRepository.save(existingBooker);
  }

  public void deleteBooker(UUID id) {
    bookerRepository.deleteById(id);
  }

}
