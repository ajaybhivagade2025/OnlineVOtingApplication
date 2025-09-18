package com.onlineVotingApplication2.repository;

import com.onlineVotingApplication2.entity.Vote;
import com.onlineVotingApplication2.entity.Voter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface VoterRepository extends JpaRepository<Voter,Long> {

    Optional<Voter> findByNationalId(String nationalId);
}
