package com.cumulusclouds.w4153cumuluscloudsmsusermanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.model.Musician;
import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.repository.MusicianRepository;

@Service
@Transactional
public class MusicianService {

  @Autowired
  private MusicianRepository musicianRepository;

  public List<Musician> getAllMusicians() {
    return musicianRepository.findAll();
  }

  public Musician getMusicianById(UUID id) {
    return musicianRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Musician not found"));
  }

  public Musician createMusician(Musician musician) {
    return musicianRepository.save(musician);
  }

  public Musician updateMusician(UUID id, Musician updatedMusician) {
    Musician existingMusician = getMusicianById(id);
    existingMusician.setGenre(updatedMusician.getGenre());
    existingMusician.setInstrumentsPlayed(updatedMusician.getInstrumentsPlayed());
    existingMusician.setYearsOfExperience(updatedMusician.getYearsOfExperience());
    existingMusician.setSampleWorks(updatedMusician.getSampleWorks());
    existingMusician.setAvailability(updatedMusician.getAvailability());
    return musicianRepository.save(existingMusician);
    }

  public void deleteMusician(UUID id) {
    musicianRepository.deleteById(id);
  }
  
}
