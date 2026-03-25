/*
package com.onlineVotingApplication2.controller;

import com.onlineVotingApplication2.entity.Candidate;
import com.onlineVotingApplication2.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @GetMapping
    public String viewCandidates(Model model) {
        List<Candidate> candidates = candidateService.getAllCandidates();
        model.addAttribute("candidates", candidates);
        model.addAttribute("candidate", new Candidate()); // For the form
        return "admin/candidates";
    }

   */
/* @PostMapping("/add")
    public String addCandidate(@ModelAttribute Candidate candidate, RedirectAttributes redirectAttributes) {
        try {
            candidateService.saveCandidate(candidate);
            redirectAttributes.addFlashAttribute("successMessage", "Candidate added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding candidate: " + e.getMessage());
        }
        return "redirect:/admin/candidates";
    }*//*


    @PostMapping("/update")
    public String updateCandidate(@ModelAttribute Candidate candidate, RedirectAttributes redirectAttributes) {
        try {
            candidateService.updateCandidate(candidate);
            redirectAttributes.addFlashAttribute("successMessage", "Candidate updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating candidate: " + e.getMessage());
        }
        return "redirect:/admin/candidates";
    }

    @PostMapping("/delete/{id}")
    public String deleteCandidate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            candidateService.deleteCandidate(id);
            redirectAttributes.addFlashAttribute("successMessage", "Candidate deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting candidate: " + e.getMessage());
        }
        return "redirect:/admin/candidates";
    }

    @PostMapping("/increment-vote/{id}")
    public String incrementVote(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            candidateService.incrementVoteCount(id);
            redirectAttributes.addFlashAttribute("successMessage", "Vote count incremented successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error incrementing vote: " + e.getMessage());
        }
        return "redirect:/admin/candidates";
    }

    @PostMapping("/reset-votes/{id}")
    public String resetVotes(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            candidateService.resetVoteCount(id);
            redirectAttributes.addFlashAttribute("successMessage", "Vote count reset successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error resetting votes: " + e.getMessage());
        }
        return "redirect:/admin/candidates";
    }
}*/
