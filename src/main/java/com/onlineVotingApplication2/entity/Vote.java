package com.onlineVotingApplication2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "vote")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vote {




    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Voter voter;

    @ManyToOne(fetch = FetchType.LAZY)
    private Candidate candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "election_id")
    private Election election;


    private LocalDateTime timestamp;
}


