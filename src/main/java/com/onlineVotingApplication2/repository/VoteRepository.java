package com.onlineVotingApplication2.repository;

import com.onlineVotingApplication2.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByVoterId(Long voterId);


    // Custom query to get vote count by candidate
    @Query("SELECT v.candidate, COUNT(v) FROM Vote v GROUP BY v.candidate ORDER BY COUNT(v) DESC")
    List<Object[]> getVoteCountByCandidate();

    boolean existsByVoterIdAndElectionId(Long voterId, Long electionId);

    // All votes for a specific election (using direct election reference)
    List<Vote> findByElectionIdOrderByIdAsc(Long electionId);

    // Optional: all votes by a specific voter
    List<Vote> findByVoterId(Long voterId);

    // Optional: all votes for a specific candidate
    List<Vote> findByCandidateId(Long candidateId);

}