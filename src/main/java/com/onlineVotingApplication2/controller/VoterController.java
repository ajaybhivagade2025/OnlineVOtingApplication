
package com.onlineVotingApplication2.controller;

import com.onlineVotingApplication2.entity.Candidate;
import com.onlineVotingApplication2.entity.Election;
import com.onlineVotingApplication2.entity.Voter;
import com.onlineVotingApplication2.service.BlockchainService;
import com.onlineVotingApplication2.service.CandidateService;
import com.onlineVotingApplication2.service.ElectionService;
import com.onlineVotingApplication2.service.VoterService;
import com.onlineVotingApplication2.utility.CandidateResultDTO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/voter")
public class VoterController {

    @Autowired
    private VoterService voterService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private ElectionService electionService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        model.addAttribute("voter", new Voter());
        return "voter/register";
    }

    @PostMapping("/register")
    public String registerVoter(
            @Valid @ModelAttribute("voter") Voter voter,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Please correct the highlighted fields.");
            return "redirect:/voter/register";
        }

        try {
            voterService.registerVoter(voter);
            redirectAttributes.addFlashAttribute("success",
                    "Registration successful! Please wait for admin approval.");
            return "redirect:/voter/login";
        } catch (DataIntegrityViolationException e) {
            Throwable root = e.getRootCause();
            String msg = root != null ? root.getMessage().toLowerCase() : "";
            if (msg.contains("duplicate") || msg.contains("unique") || msg.contains("national_id")) {
                redirectAttributes.addFlashAttribute("error", "A voter with this National ID already exists.");
            } else {
                redirectAttributes.addFlashAttribute("error", "An unexpected error occurred. Please try again.");
            }
            return "redirect:/voter/register";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred. Please try again.");
            return "redirect:/voter/register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "voter/login";
    }

    @PostMapping("/login")
    public String loginVoter(@RequestParam String nationalId,
                             @RequestParam String password,
                             HttpSession session,
                             RedirectAttributes attributes) {

        Optional<Voter> voterOpt = voterService.loginVoter(nationalId, password);
        if (voterOpt.isPresent()) {
            Voter voter = voterOpt.get();
            session.setAttribute("voter", voter); // Always store as "voter"
            return "redirect:/voter/dashboard";
        }

        attributes.addFlashAttribute("error",
                "Invalid credentials or account not verified by admin");
        return "redirect:/voter/login";
    }


    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Voter voter = (Voter) session.getAttribute("voter");
        if (voter == null) return "redirect:/voter/login";

        // Fetch ongoing elections
        List<Election> ongoingElections = electionService.getOngoingElections();
        model.addAttribute("ongoingElections", ongoingElections);

        model.addAttribute("voter", voter);
        return "voter/dashboard";
    }



    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/voter/login";
    }

    @GetMapping("/profile")
    public String viewProfile(HttpSession session, Model model) {
        Voter voter = (Voter) session.getAttribute("voter");
        if (voter == null) return "redirect:/voter/login";
        model.addAttribute("voter", voter);
        return "voter/profile";
    }

    // Show all candidates to voter
    @GetMapping("/candidates")
    public String showCandidatesForVoter(Model model, HttpSession session) {
        Voter voter = (Voter) session.getAttribute("voter");
        if (voter == null) return "redirect:/voter/login";

        List<Candidate> candidates = candidateService.getAllCandidates();
        model.addAttribute("candidates", candidates);
        model.addAttribute("voter", voter); // Required for hasVoted & votedCandidateId
        return "voter/candidates";
    }
    // ✅ Show candidates for a specific election
    @GetMapping("/election/{electionId}/candidates")
    public String showCandidates(@PathVariable Long electionId, Model model, HttpSession session) {
        Voter voter = (Voter) session.getAttribute("voter");
        if (voter == null) return "redirect:/voter/login";

        List<Candidate> candidates = candidateService.getCandidatesByElectionId(electionId);

        model.addAttribute("candidates", candidates);
        model.addAttribute("voter", voter);
        model.addAttribute("electionId", electionId);

        return "voter/candidates"; // Thymeleaf page
    }

    // ✅ Cast vote
    @PostMapping("/election/{electionId}/vote/{candidateId}")
    public String castVote(@PathVariable Long electionId,
                           @PathVariable Long candidateId,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {

        Voter voter = (Voter) session.getAttribute("voter");
        if (voter == null) {
            redirectAttributes.addFlashAttribute("error", "Please login first.");
            return "redirect:/voter/login";
        }

        if (voterService.hasVotedInElection(voter.getId(), electionId)) {
            redirectAttributes.addFlashAttribute("error", "You have already voted in this election!");
            return "redirect:/voter/election/" + electionId + "/candidates";
        }

        Optional<Candidate> candidateOpt = candidateService.getCandidateById(candidateId);
        if (candidateOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Candidate not found.");
            return "redirect:/voter/election/" + electionId + "/candidates";
        }

        Candidate candidate = candidateOpt.get();

        // 1️⃣ Increment candidate vote count
        candidateService.incrementVoteCount(candidateId);

        // 2️⃣ Save vote in DB
        voterService.saveVote(voter.getId(), candidateId, electionId);

        // 3️⃣ Add vote to blockchain
        String blockData = "ElectionID:" + electionId + ", CandidateID:" + candidateId + ", VoterID:" + voter.getId();
        blockchainService.addVoteBlock(electionId, candidateId, voter.getId(), blockData);

        // 4️⃣ Mark voter as voted for this election
        voterService.markVoterAsVoted(voter.getId(), electionId);

        // Update session voter info
        session.setAttribute("voter", voter);

        redirectAttributes.addFlashAttribute("success", "Your vote has been recorded successfully!");
        return "redirect:/voter/election/" + electionId + "/candidates";
    }


    @GetMapping("/voter-result")
    public String showVoterResults(Model model, HttpSession session) {

        // 1️⃣ Logged-in voter
        Voter voter = (Voter) session.getAttribute("voter");
        model.addAttribute("voter", voter);

        // 2️⃣ Latest election
        Election election = electionService.getLatestElection();
        if (election == null) {
            model.addAttribute("message1", "No elections available at the moment.");
            return "voter/no-results";
        }

        // 3️⃣ Check if election ended (admin has completed it)
        if (election.getStatus() != Election.Status.COMPLETED) {
            model.addAttribute("message2", "Results will be available after voting ends by the admin.");
            model.addAttribute("election", election);
            return "voter/no-results";
        }

        // 4️⃣ Optional: restrict if voter hasn't voted
        boolean restrictIfNotVoted = false; // change true to require vote
        if (restrictIfNotVoted && (voter == null || !voter.isHasVoted())) {
            model.addAttribute("message", "You must vote to view the results.");

            return "voter/voter-result";
        }

        // 5️⃣ Fetch candidate results & convert to DTO
        List<CandidateResultDTO> results = candidateService.findByElectionOrderByVoteCountDesc(election.getId())
                .stream()
                .map(c -> new CandidateResultDTO(c.getName(), c.getVoteCount(),c.getParty(),c.getLogo()))
                .collect(Collectors.toList());

        // 6️⃣ Verify blockchain
        boolean chainValid = blockchainService.verifyChain();

        // 7️⃣ Add attributes to model
        model.addAttribute("election",election.getName());
        model.addAttribute("results", results);
        model.addAttribute("election", election);
        model.addAttribute("chainValid", chainValid);

        return "voter/voter-result"; // NO leading slash
    }



}




