
package com.onlineVotingApplication2.service;

import com.onlineVotingApplication2.entity.Candidate;
import com.onlineVotingApplication2.entity.Election;
import com.onlineVotingApplication2.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CandidateService {

    @Autowired
    private ElectionService electionService;

    @Autowired
    private CandidateRepository candidateRepository;

    private ConcurrentHashMap<Long, Long> votesMap = new ConcurrentHashMap<>();

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Optional<Candidate> getCandidateById(Long id) {
        return candidateRepository.findById(id);
    }

    public Candidate saveCandidate(Candidate candidate) {
//        if (candidateRepository.existsByParty(candidate.getParty())) {
//            throw new RuntimeException("Party '" + candidate.getParty() + "' already exists. Please choose a different party name.");
//        }
        return candidateRepository.save(candidate);
    }

    public Candidate updateCandidate(Candidate candidate) {
        Optional<Candidate> existingCandidate = candidateRepository.findById(candidate.getId());
        if (existingCandidate.isPresent()) {
            Candidate c = existingCandidate.get();
            c.setName(candidate.getName());
            c.setParty(candidate.getParty());
            if (candidate.getVoteCount() >= 0) c.setVoteCount(candidate.getVoteCount());
            return candidateRepository.save(c);
        } else {
            throw new RuntimeException("Candidate not found with id: " + candidate.getId());
        }
    }

    public void deleteCandidate(Long id) {
        candidateRepository.deleteById(id);
    }

    public List<Candidate> getCandidatesByElectionId(Long electionId) {
        Election election = electionService.getElectionById(electionId).orElse(null);
        if (election != null) {
            return candidateRepository.findByElection(election);
        }
        return List.of();
    }

    public List<Candidate> getAllCandidatesWithVoteCount() {
        return candidateRepository.findAllByOrderByVoteCountDesc();
    }

    public long count() {
        return candidateRepository.count();
    }

    // =================== Voter Operations ===================

    // ✅ Increment vote count safely during voting
    public void incrementVoteCount(Long candidateId) {
        Optional<Candidate> candidateOpt = candidateRepository.findById(candidateId);
        if (candidateOpt.isPresent()) {
            Candidate c = candidateOpt.get();
            c.setVoteCount(c.getVoteCount() + 1);
            candidateRepository.save(c);
        } else {
            throw new RuntimeException("Candidate not found with id: " + candidateId);
        }
    }

    // ✅ Reset vote count (if needed)
    public void resetVoteCount(Long candidateId) {
        Optional<Candidate> candidateOpt = candidateRepository.findById(candidateId);
        if (candidateOpt.isPresent()) {
            Candidate c = candidateOpt.get();
            c.setVoteCount(0);
            candidateRepository.save(c);
        } else {
            throw new RuntimeException("Candidate not found with id: " + candidateId);
        }
    }

    // =================== Optional In-Memory Voter Tracking ===================
    public boolean voteForCandidate(Long candidateId, Long voterId) {
        if (votesMap.containsKey(voterId)) return false;
        Optional<Candidate> candidateOpt = candidateRepository.findById(candidateId);
        if (candidateOpt.isEmpty()) return false;

        Candidate c = candidateOpt.get();
        c.setVoteCount(c.getVoteCount() + 1);
        candidateRepository.save(c);

        votesMap.put(voterId, candidateId);
        return true;
    }

    public Long getVotedCandidateId(Long voterId) {
        return votesMap.get(voterId);
    }


    public List<Candidate> findByElectionOrderByVoteCountDesc(Long electionId) {
        return candidateRepository.findByElectionOrderByVoteCountDesc(electionId);
    }
}
