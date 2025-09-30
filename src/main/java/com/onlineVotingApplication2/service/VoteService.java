package com.onlineVotingApplication2.service;

import com.onlineVotingApplication2.entity.Candidate;
import com.onlineVotingApplication2.entity.Vote;
import com.onlineVotingApplication2.entity.Voter;
import com.onlineVotingApplication2.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private VoterService voterService;

    @Autowired
    private BlockchainService blockchainService;


    public Vote saveVote(Vote vote) {
        return voteRepository.save(vote);
    }


    public boolean hasVoted(Long voterId) {
        return voteRepository.existsByVoterId(voterId);
    }

    public long getTotalVotes() {
        return voteRepository.count();
    }


    public Map<String, Long> getVoteCountByCandidateMap() {
        List<Object[]> results = voteRepository.getVoteCountByCandidate();

        return results.stream()
                .collect(Collectors.toMap(
                        result -> ((Candidate) result[0]).getName() + " (" + ((Candidate) result[0]).getParty() + ")",
                        result -> (Long) result[1]
                ));
    }
    public List<CandidateVoteResult> getCandidateVoteResults() {
        List<Object[]> results = voteRepository.getVoteCountByCandidate();

        return results.stream()
                .map(result -> new CandidateVoteResult(
                        (Candidate) result[0],
                        (Long) result[1]
                ))
                .collect(Collectors.toList());
    }
    // NEW: DTO for candidate vote results
    public static class CandidateVoteResult {
        private Candidate candidate;
        private Long voteCount;

        public CandidateVoteResult(Candidate candidate, Long voteCount) {
            this.candidate = candidate;
            this.voteCount = voteCount;
        }

        // Getters
        public Candidate getCandidate() { return candidate; }
        public Long getVoteCount() { return voteCount; }

        // Helper methods for Thymeleaf
        public String getCandidateName() { return candidate.getName(); }
        public String getCandidateParty() { return candidate.getParty(); }
        public Long getCandidateId() { return candidate.getId(); }
    }

    public long count() {
        return voteRepository.count(); // ✅ Total Candidate rows
    }










}