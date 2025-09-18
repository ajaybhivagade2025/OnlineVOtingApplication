package com.onlineVotingApplication2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Voter {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(unique = true, nullable = false)
        private String nationalId; // CNIC/Aadhaar
        private String name;
        private String email;
        private String mobile;
        private boolean verified = false;
        private boolean hasVoted = false;
        private String otp;
        private LocalDateTime otpExpiry;

    }






