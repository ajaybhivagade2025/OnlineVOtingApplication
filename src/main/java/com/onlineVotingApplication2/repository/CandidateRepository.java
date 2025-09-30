package com.onlineVotingApplication2.repository;

import com.onlineVotingApplication2.entity.Candidate;
import com.onlineVotingApplication2.entity.Election;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    List<Candidate> findByParty(String party);
    Candidate findByName(String name);
    List<Candidate> findAllByOrderByVoteCountDesc();
    boolean existsByParty(String party);
    // Find all candidates by election
    List<Candidate> findByElection(Election election);


    // Election ke saare candidates voteCount ke descending order me
    @Query("SELECT c FROM Candidate c WHERE c.election.id = :electionId ORDER BY c.voteCount DESC")
    List<Candidate> findByElectionOrderByVoteCountDesc(@Param("electionId") Long electionId);
}

