package com.onlineVotingApplication2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "candidate")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Candidate {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

    @NotBlank(message = "name is required")
        private String name;
        private String party;
        private int voteCount = 0;
        private String logo;


    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Vote> votes;

    @ManyToOne
    @JoinColumn(name = "election_id")
    private Election election;

}



