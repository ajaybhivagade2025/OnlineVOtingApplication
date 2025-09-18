package com.onlineVotingApplication2.repository;

import com.onlineVotingApplication2.entity.Block;
import com.onlineVotingApplication2.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockRepository extends JpaRepository<Block,Long> {

    Optional<Block> findTopByOrderByIdDesc();
    Optional<Block> findByCurrentHash(String currentHash);

}


