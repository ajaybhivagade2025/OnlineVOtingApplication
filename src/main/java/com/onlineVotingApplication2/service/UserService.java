package com.onlineVotingApplication2.service;

import com.onlineVotingApplication2.entity.User;
import com.onlineVotingApplication2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createAdmin(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

   /* public User saveUser(User user) {
        return userRepository.save(user);
    }*/
}