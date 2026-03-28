package com.onlineVotingApplication2.service;


import com.onlineVotingApplication2.entity.Election;
import com.onlineVotingApplication2.repository.ElectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ElectionService {

    private final ElectionRepository electionRepository;

    public ElectionService(ElectionRepository electionRepository) {
        this.electionRepository = electionRepository;
    }

    public Election getLatestElection() {
        return electionRepository.findTopByOrderByIdDesc();
    }

    public Election saveElection(Election election) {
        return electionRepository.save(election);
    }

    public void updateStatus(Election election, Election.Status status) {
        election.setStatus(status);
        electionRepository.save(election);
    }

    public List<Election> getAllElection(){
       return electionRepository.findAll();
    }

    // Get election by ID
    public Optional<Election> getElectionById(Long id) {
        return electionRepository.findById(id);
    }

    public List<Election> getOngoingElections() {
        return electionRepository.findByStatus(Election.Status.ONGOING);
    }


    public void deleteElection(Long id) {
        electionRepository.deleteById(id);
    }
}

