package com.onlineVotingApplication2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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


    private Long electionId;


    public void generateHash() {
        this.currentHash = calculateHash();
    }

   /* public String calculateHash() {
        String input = previousHash + timestamp + data;
        return DigestUtils.sha256Hex(input);
    }*/

    public String calculateHash() {
        // FIXED FORMAT for timestamp to avoid DB formatting issues
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timeString = timestamp.format(fmt);
        String input = previousHash + timeString + data;
        return DigestUtils.sha256Hex(input);
    }

}


