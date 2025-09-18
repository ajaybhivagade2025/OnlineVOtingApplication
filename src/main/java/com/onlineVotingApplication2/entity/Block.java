package com.onlineVotingApplication2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "block")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Block {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String previousHash;
        private String currentHash;
        @Column(length = 2000)
        private String data;
        private LocalDateTime timestamp;
    }


