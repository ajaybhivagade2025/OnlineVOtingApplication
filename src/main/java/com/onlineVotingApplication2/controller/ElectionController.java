package com.onlineVotingApplication2.controller;

import com.onlineVotingApplication2.entity.Candidate;
import com.onlineVotingApplication2.entity.Election;
import com.onlineVotingApplication2.service.CandidateService;
import com.onlineVotingApplication2.service.ElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ElectionController {

    @Autowired
    private ElectionService electionService;

    @Autowired
    private CandidateService candidateService;

    // Election create form
    @GetMapping("/admin/election/create")
    public String showCreateElectionForm(Model model) {
        model.addAttribute("election", new Election());
        return "admin/create-election";
    }

    // Save election
    @PostMapping("/admin/election/create")
    public String createElection(@ModelAttribute("election") Election election, Model model) {
        // Set default status
        election.setStatus(Election.Status.NOT_STARTED);
        electionService.saveElection(election);
        model.addAttribute("message", "Election created successfully!");
        return "admin/create-election";
    }

    // For LocalDateTime binding from datetime-local input
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalDateTime.parse(text, formatter));
            }
        });
    }


    @GetMapping("/admin/election/results/{id}")
    public String viewElectionResults(@PathVariable Long id, Model model) {
        Election election = electionService.getElectionById(id).orElse(null);
        if (election == null || election.getStatus() != Election.Status.COMPLETED) {
            model.addAttribute("message", "Results will be available after election is completed.");
            return "admin/no-results";
        }
        List<Candidate> candidates = candidateService.getCandidatesByElectionId(id);
        model.addAttribute("candidates", candidates);
        model.addAttribute("election", election);
        return "admin/election-results";
    }

    @GetMapping("admin/elections")
    public String listAllElections(Model model){
        List<Election> elections=electionService.getAllElection();

        List<Map<String, String>> electionList = elections.stream().map(e -> {
            Map<String, String> map = new HashMap<>();
            map.put("id", e.getId().toString());
            map.put("name", e.getName());
            map.put("startTime", e.getStartTime() != null ? e.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "Not Set");
            map.put("endTime", e.getEndTime() != null ? e.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "Not Set");
            map.put("status", e.getStatus() != null ? e.getStatus().name() : "N/A");
            return map;
        }).collect(Collectors.toList());

        model.addAttribute("elections", electionList);

        return "admin/list-elections";
    }

    // start election


    @GetMapping("/admin/election/start/{id}")
    public String startElection(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Election election = electionService.getElectionById(id).orElse(null);

        if (election == null) {
            redirectAttributes.addFlashAttribute("error", "Election not found");
            return "redirect:/admin/elections";
        }

        if (election.getStatus() != Election.Status.NOT_STARTED) {
            redirectAttributes.addFlashAttribute("error", "Election cannot be started");
            return "redirect:/admin/elections";
        }

        election.setStatus(Election.Status.ONGOING);
        electionService.saveElection(election);

        redirectAttributes.addFlashAttribute("success", "Election started successfully");
        return "redirect:/admin/elections";
    }

    // END ELECTION


    @GetMapping("/admin/election/end/{id}")
    public String endElection(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Election election = electionService.getElectionById(id).orElse(null);

        if (election == null) {
            redirectAttributes.addFlashAttribute("error", "Election not found");
            return "redirect:/admin/elections";
        }

        if (election.getStatus() != Election.Status.ONGOING) {
            redirectAttributes.addFlashAttribute("error", "Election is not ongoing");
            return "redirect:/admin/elections";
        }

        election.setStatus(Election.Status.COMPLETED);
        electionService.saveElection(election);

        redirectAttributes.addFlashAttribute("success", "Election ended successfully");
        return "redirect:/admin/elections";
    }

    @GetMapping("/admin/election/delete/{id}")
    public String deleteElection(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        Election election = electionService.getElectionById(id).orElse(null);

        if (election == null) {
            redirectAttributes.addFlashAttribute("error", "Election not found");
            return "redirect:/admin/elections";
        }

        // ❌ Optional safety: ONGOING election delete na ho
        if (election.getStatus() == Election.Status.ONGOING) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete ongoing election");
            return "redirect:/admin/elections";
        }

        electionService.deleteElection(id);

        redirectAttributes.addFlashAttribute("success", "Election deleted successfully");
        return "redirect:/admin/elections";
    }



}
