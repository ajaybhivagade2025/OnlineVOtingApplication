package com.onlineVotingApplication2.service;

import com.onlineVotingApplication2.entity.Block;
import com.onlineVotingApplication2.entity.Candidate;
import com.onlineVotingApplication2.entity.Vote;
import com.onlineVotingApplication2.entity.Voter;
import com.onlineVotingApplication2.repository.CandidateRepository;
import com.onlineVotingApplication2.repository.VoteRepository;
import com.onlineVotingApplication2.repository.VoterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VotingService {


        @Autowired
        private VoterRepository voterRepo;
        @Autowired private CandidateRepository candidateRepo;
        @Autowired private VoteRepository voteRepo;
        @Autowired private BlockchainService blockchainService;
        @Autowired private NotificationService notificationService;

        public String castVote(String nationalId, Long candidateId) {
            Voter voter = voterRepo.findByNationalId(nationalId)
                    .orElseThrow(() -> new RuntimeException("Voter not found"));

            if (!voter.isVerified()) throw new RuntimeException("Voter not verified");
            if (voter.isHasVoted()) throw new RuntimeException("Voter already voted");

            Candidate candidate = candidateRepo.findById(candidateId)
                    .orElseThrow(() -> new RuntimeException("Candidate not found"));

            // update counts
            voter.setHasVoted(true);
            candidate.setVoteCount(candidate.getVoteCount() + 1);
            voterRepo.save(voter);
            candidateRepo.save(candidate);

            // record vote
            Vote vote = Vote.builder()
                    .voter(voter)
                    .candidate(candidate)
                    .timestamp(LocalDateTime.now())
                    .build();
            voteRepo.save(vote);

            // create block
            String data = "voter=" + voter.getNationalId() + "|candidateId=" + candidate.getId() + "|ts=" + vote.getTimestamp();
            Block block = blockchainService.addBlock(data);

            // notify voter with transaction hash
            String message = "Your vote is recorded. Transaction hash: " + block.getCurrentHash();
            notificationService.notifyByEmail(voter.getEmail(), message);
            notificationService.notifyBySms(voter.getMobile(), message);

            return block.getCurrentHash();
        }
    }


