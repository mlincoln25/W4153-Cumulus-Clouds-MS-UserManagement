package com.cumulusclouds.w4153cumuluscloudsmsusermanagement.service;

import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.model.User;
import com.cumulusclouds.w4153cumuluscloudsmsusermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
