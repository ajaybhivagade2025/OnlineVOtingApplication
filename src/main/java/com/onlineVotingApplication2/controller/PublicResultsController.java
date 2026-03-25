package com.onlineVotingApplication2.controller;

import com.onlineVotingApplication2.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class PublicResultsController {



        @Autowired
        private VoteService voteService;

        @GetMapping("/results")
        public String publicResults(Model model) {
            List<VoteService.CandidateVoteResult> results = voteService.getCandidateVoteResults();
            model.addAttribute("candidateResults", results);
            model.addAttribute("totalVotes", voteService.getTotalVotes());
            return "vote/public-results";
        }
    }


