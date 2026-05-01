package com.onlineVotingApplication2.controller;

import com.onlineVotingApplication2.entity.*;
import com.onlineVotingApplication2.repository.BlockRepository;
import com.onlineVotingApplication2.repository.CandidateRepository;
import com.onlineVotingApplication2.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/vote")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private VoterService voterService;

    @Autowired
    private VotingService castVote;


    @Autowired
    private ElectionService electionService;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private BlockchainService blockchainService;
    @Autowired
    private BlockRepository blockRepository;


    @GetMapping
    public String showVotingPage(HttpSession session, Model model) {
        Voter voter = (Voter) session.getAttribute("voter");
        if (voter == null) return "redirect:/voter/login";

        if (voter.isHasVoted()) {
            model.addAttribute("error", "You have already voted.");
            return "voter/dashboard";
        }

        if (!voter.isVerified()) {
            model.addAttribute("error", "Your account is not verified yet. Please wait for admin approval.");
            return "voter/dashboard";
        }

        List<Candidate> candidates = candidateService.getAllCandidates();
        model.addAttribute("candidates", candidates);
        model.addAttribute("voter", voter);

        return "vote/voting-page";
    }



    @GetMapping("/results")
    public String showResults(Model model) {
        Election election = electionService.getLatestElection();

        if (election == null || election.getStatus() != Election.Status.COMPLETED) {
            model.addAttribute("message", "Results will be available after voting ends.");
            return "vote/no-results";
        }

        List<Candidate> candidates = candidateRepository.findAllByOrderByVoteCountDesc();
        model.addAttribute("candidates", candidates);
        return "vote/results";
    }



    @GetMapping("/elections")
    public String listOngoingElections(Model model) {
        List<Election> elections = electionService.getAllElection();
        model.addAttribute("elections", elections);
        return "voter/elections";
    }


    // ================= LIST CANDIDATES =================
    @GetMapping("/election/{id}/candidates")
    public String listCandidates(@PathVariable Long id, Model model, HttpSession session) {
        Election election = electionService.getElectionById(id).orElse(null);
        if (election == null) return "redirect:/voter/elections";

        List<Candidate> candidates = candidateService.getCandidatesByElectionId(id);
        Voter voter = (Voter) session.getAttribute("voterUser");
        boolean hasVoted = voter != null && voter.isHasVoted() && voter.getVotedCandidateId() != null;

        model.addAttribute("candidates", candidates);
        model.addAttribute("election", election);
        model.addAttribute("hasVoted", hasVoted);
        model.addAttribute("voter", voter);
        return "voter/candidates";
    }

    @GetMapping("/election/{electionId}/vote/{candidateId}")
    public String castVote(@PathVariable Long electionId,
                           @PathVariable Long candidateId,
                           HttpSession session,
                           Model model) {
        System.out.println("➡️ Enter castVote()");
        System.out.println("electionId=" + electionId + ", candidateId=" + candidateId);

        Voter voter = (Voter) session.getAttribute("voterUser");
        System.out.println("Session Voter = " + voter);

        if (voter == null) {
            System.out.println("No voter in session");
            return "redirect:/voter/login";
        }

        if (voter.isHasVoted()) {
            System.out.println("Already voted");
            model.addAttribute("error", "You have already voted!");
            return "redirect:/voter/election/" + electionId + "/candidates";
        }

        Candidate candidate = candidateService.getCandidateById(candidateId).orElse(null);
        System.out.println("Candidate = " + candidate);
        if (candidate == null) {
            System.out.println("Candidate not found");
            return "redirect:/voter/elections";
        }

        // ✅ Increment vote count
        candidate.setVoteCount(candidate.getVoteCount() + 1);
        candidateService.saveCandidate(candidate);
        System.out.println("✅ Candidate vote updated");

        // ✅ Save vote in DB
        Vote vote = Vote.builder()
                .candidate(candidate)
                .voter(voter)
                .timestamp(LocalDateTime.now())
                .build();
        voteService.saveVote(vote);
        System.out.println("✅ Vote record saved");

        // ✅ Add vote to blockchain
        String blockData = "ElectionID:" + electionId +
                ", CandidateID:" + candidateId +
                ", VoterID:" + voter.getId();
        blockchainService.addVoteBlock(electionId, candidateId, voter.getId(), blockData);
        System.out.println("✅ Blockchain block added");

        // ✅ Mark voter as voted
        voter.setHasVoted(true);
        voter.setVotedCandidateId(candidateId);
        voterService.saveVoter(voter);
        System.out.println("✅ Voter updated");

        System.out.println("⬅️ Leaving castVote()");
        return "redirect:/voter/elections";
    }




}