package com.onlineVotingApplication2.repository;

import com.onlineVotingApplication2.entity.Voter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoterRepository extends JpaRepository<Voter, Long> {
    Optional<Voter> findByNationalId(String nationalId);
    List<Voter> findByVerified(boolean verified);
    List<Voter> findByHasVoted(boolean hasVoted);
    boolean existsByNationalId(String nationalId);
    boolean existsByEmail(String email);
    long countByVerifiedFalse();
}