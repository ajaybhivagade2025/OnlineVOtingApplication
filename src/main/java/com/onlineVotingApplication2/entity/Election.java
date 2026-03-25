package com.onlineVotingApplication2.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "elections")
public class Election {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;                  // Election ka naam (e.g. "Student Council 2025")
    private LocalDateTime startTime;      // Start date/time
    private LocalDateTime endTime;        // End date/time

    @Enumerated(EnumType.STRING)
    private Status status;                // NOT_STARTED, ONGOING, COMPLETED

    public enum Status {
        NOT_STARTED,
        ONGOING,
        COMPLETED
    }

}