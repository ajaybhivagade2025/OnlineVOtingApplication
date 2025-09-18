package com.onlineVotingApplication2.repository;

import com.onlineVotingApplication2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository  extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String admin);
}
