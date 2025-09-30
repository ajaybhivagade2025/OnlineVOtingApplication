package com.onlineVotingApplication2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "voters")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Voter {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
       @Column(unique = true)
       @NotBlank(message = "National ID is required")
       @Size(min = 8, max = 20)
       private String nationalId;

    @NotBlank(message = "Name is required")
        private String name;

    @Email(message = "Invalid email")
        private String email;

    @NotBlank(message = "mobile is required")
        private String mobile;

        @NotBlank(message = "Password is required")
        private String password;


        private boolean verified = false;
        private boolean hasVoted = false;

      private Long votedCandidateId;

    }






