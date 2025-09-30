package com.onlineVotingApplication2.repository;

import com.onlineVotingApplication2.entity.Election;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ElectionRepository extends JpaRepository<Election, Long> {


    Election findTopByOrderByIdDesc();

    List<Election> findByStatus(Election.Status status);

}
