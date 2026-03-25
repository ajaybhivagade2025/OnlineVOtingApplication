package com.onlineVotingApplication2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "candidate")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

    @NotBlank(message = "name is required")
        private String name;

        private String party;

        private int voteCount = 0;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes;

    @ManyToOne
    @JoinColumn(name = "election_id")
    private Election election;

}



