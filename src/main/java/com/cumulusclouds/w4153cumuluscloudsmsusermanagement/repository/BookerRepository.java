package com.cumulusclouds.w4153cumuluscloudsmsusermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.model.Booker;

@Repository
public interface BookerRepository extends JpaRepository<Booker, UUID> {

}