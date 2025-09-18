package com.onlineVotingApplication2.repository;

import com.onlineVotingApplication2.entity.Candidate;
import com.onlineVotingApplication2.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate,Long> {
}
