package com.onlineVotingApplication2.service;

import com.onlineVotingApplication2.entity.Candidate;
import com.onlineVotingApplication2.entity.Vote;
import com.onlineVotingApplication2.entity.Voter;
import com.onlineVotingApplication2.repository.CandidateRepository;
import com.onlineVotingApplication2.repository.VoteRepository;
import com.onlineVotingApplication2.repository.VoterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VoterService {

    @Autowired
    private VoterRepository voterRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    public Voter registerVoter(Voter voter) {
        // Encrypt password before saving
        return voterRepository.save(voter);
    }

    public Optional<Voter> loginVoter(String nationalId, String password) {
        Optional<Voter> voterOpt = voterRepository.findByNationalId(nationalId);
        if (voterOpt.isPresent()) {
            Voter voter = voterOpt.get();
            // Check if password matches and voter is verified
            if (voter.getPassword().equals(password) && voter.isVerified()) {
                return Optional.of(voter);
            }
        }
        return Optional.empty();
    }

    public Optional<Voter> getVoterById(Long id) {
        return voterRepository.findById(id);
    }

    public Optional<Voter> getVoterByNationalId(String nationalId) {
        return voterRepository.findByNationalId(nationalId);
    }

    public List<Voter> getAllVoters() {
        return voterRepository.findAll();
    }

    public List<Voter> getUnverifiedVoters() {
        return voterRepository.findByVerified(false);
    }

    public Voter updateVoter(Voter voter) {
        return voterRepository.save(voter);
    }

    public void deleteVoter(Long id) {
        voterRepository.deleteById(id);
    }

   @Transactional
   public void approveVoter(Long id) {
       Voter v = voterRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("Voter not found"));
       // maybe check if already verified
       v.setVerified(true);
       // If there are other fields that must be non-null, make sure they're present
       voterRepository.save(v);
   }

    public boolean markVoted(Long voterId) {
        Optional<Voter> voterOpt = voterRepository.findById(voterId);
        if (voterOpt.isPresent()) {
            Voter voter = voterOpt.get();
            voter.setHasVoted(true);
            voterRepository.save(voter);
            return true;
        }
        return false;
    }

    public boolean hasVoted(String nationalId) {
        Optional<Voter> voterOpt = voterRepository.findByNationalId(nationalId);
        return voterOpt.map(Voter::isHasVoted).orElse(false);
    }

    public Voter saveVoter(Voter voter) {
        return voterRepository.save(voter);
    }






    public void markVoterAsVoted(Long voterId, Long electionId) {
        Voter voter = voterRepository.findById(voterId).orElseThrow();

        // Option 1: simple DB update
        voter.setHasVoted(true); // global flag for single-election system
        voter.setVotedCandidateId(electionId); // ya candidateId, electionId ke saath
        voterRepository.save(voter);

        // Option 2: Better → create Vote table with electionId, candidateId, voterId
        // Fir yaha sirf DB me vote save karo. hasVoted flag check karna election-specific ho.
    }
    public boolean hasVotedInElection(Long voterId, Long electionId) {
        return voteRepository.existsByVoterIdAndElectionId(voterId, electionId);
    }

    public void saveVote(Long voterId, Long candidateId, Long electionId) {
        Voter voter = voterRepository.findById(voterId)
                .orElseThrow(() -> new RuntimeException("Voter not found"));
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        // Create Vote object
        Vote vote = Vote.builder()
                .voter(voter)
                .candidate(candidate)
                .timestamp(LocalDateTime.now())
                .build();

        vote.setElection(candidate.getElection());

        voteRepository.save(vote); // Save in DB
    }

    public long count() {
        return voterRepository.count(); // ✅ Total Candidate rows
    }


    public long countPending() {
        return voterRepository.countByVerifiedFalse(); // ✅ Pending verifications
    }



}