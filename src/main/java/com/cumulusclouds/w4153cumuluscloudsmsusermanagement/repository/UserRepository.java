package com.cumulusclouds.w4153cumuluscloudsmsusermanagement.repository;

import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository; 

public interface UserRepository extends JpaRepository<User, Long> { 
    User findByUsername(String username);
}
