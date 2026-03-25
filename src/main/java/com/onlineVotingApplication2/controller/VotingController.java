/*
package com.onlineVotingApplication2.controller;

import com.onlineVotingApplication2.repository.CandidateRepository;
import com.onlineVotingApplication2.service.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VotingController {

        @Autowired
        private CandidateRepository candidateRepo;
        @Autowired private VotingService votingService;

        @GetMapping("/vote")
        public String showVotePage(Model model) {
            model.addAttribute("candidates", candidateRepo.findAll());
            return "vote";
        }

        @PostMapping("/vote")
        public String castVote(@RequestParam String nationalId,
                               @RequestParam Long candidateId,
                               Model model) {
            try {
                String txHash = votingService.castVote(nationalId, candidateId);
                model.addAttribute("message", "Vote cast! Transaction hash: " + txHash);
                return "result";
            } catch (RuntimeException ex) {
                model.addAttribute("error", ex.getMessage());
                model.addAttribute("candidates", candidateRepo.findAll());
                return "vote";
            }
        }
    }


*/
