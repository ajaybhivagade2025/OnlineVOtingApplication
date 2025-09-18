/*

package com.onlineVotingApplication2.config;

import com.onlineVotingApplication2.entity.Candidate;
import com.onlineVotingApplication2.entity.User;
import com.onlineVotingApplication2.repository.CandidateRepository;
import com.onlineVotingApplication2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Add admin user if not exists
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // Encode password
            admin.setRole("ADMIN");
            userRepository.save(admin);
        }

        // Add a sample verified voter if not exists
        if (userRepository.findByUsername("123456789").isEmpty()) {
            User voter1 = new User();
            voter1.setUsername("123456789"); // National ID
            voter1.setRole("VOTER");
            voter1.setVerified(true);
            voter1.setHasVoted(false);
            userRepository.save(voter1);
        }

        // Add a sample unverified voter if not exists
        if (userRepository.findByUsername("987654321").isEmpty()) {
            User voter2 = new User();
            voter2.setUsername("987654321"); // National ID
            voter2.setRole("VOTER");
            voter2.setVerified(false);
            voter2.setHasVoted(false);
            userRepository.save(voter2);
        }

        // Add sample candidates if none exist
        if (candidateRepository.count() == 0) {
            Candidate candidate1 = new Candidate();
            candidate1.setName("John Doe");
            candidate1.setParty("Independent");
            candidate1.setVoteCount(0);
            candidateRepository.save(candidate1);

            Candidate candidate2 = new Candidate();
            candidate2.setName("Jane Smith");
            candidate2.setParty("Green Party");
            candidate2.setVoteCount(0);
            candidateRepository.save(candidate2);
        }
    }
}
*/
