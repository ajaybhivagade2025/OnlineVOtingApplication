package com.onlineVotingApplication2.repository;

import com.onlineVotingApplication2.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote,Long> {
}
