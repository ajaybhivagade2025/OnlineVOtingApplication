
package com.onlineVotingApplication2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; // For admin: username, for voter: nationalId
    private String password; // For admin
    private String role; // "ADMIN" or "VOTER"

    private boolean hasVoted;
    private boolean isVerified; // For voters
}
