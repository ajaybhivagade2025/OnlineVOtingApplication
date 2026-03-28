package com.onlineVotingApplication2.controller;

import com.onlineVotingApplication2.entity.*;
import com.onlineVotingApplication2.repository.VoterRepository;
import com.onlineVotingApplication2.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private VoterService voterService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private VoterRepository voterRepository;

    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private ElectionService electionService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {

        Optional<User> adminOpt = userService.getUserByUsername(username);

        if (adminOpt.isPresent()) {
            User admin = adminOpt.get();
            if ("ADMIN".equals(admin.getRole()) && admin.getPassword().equals(password)) {
                session.setAttribute("adminUser", admin);
                return "redirect:/admin/dashboard";
            }
        }

        model.addAttribute("error", "Invalid username or password");
        return "admin/login";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }


    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {

        // Stats
        model.addAttribute("totalCandidates", candidateService.count());
        model.addAttribute("totalVoters", voterService.count());
        model.addAttribute("totalVotes", voteService.count());
        model.addAttribute("pendingVerifications", voterService.countPending());

        // ✅ Sirf ONGOING election fetch karo
        List<Election> ongoingElections = electionService.getOngoingElections();
        model.addAttribute("ongoingElections", ongoingElections);

        return "admin/dashboard";
    }

    @GetMapping("/candidates")
    public String showCandidatesPage(Model model) {
        // New Candidate object for Add Form
        model.addAttribute("candidate", new Candidate());

        // Fetch all existing candidates
        List<Candidate> candidates = candidateService.getAllCandidates();
        model.addAttribute("candidates", candidates);

        // All elections for dropdown if needed
        model.addAttribute("elections", electionService.getAllElection());

        return "admin/candidates"; // This should point to your candidates.html
    }


    @GetMapping("/add-candidate")
    public String showAddCandidateForm(Model model) {
        model.addAttribute("candidate", new Candidate());
        model.addAttribute("elections", electionService.getAllElection()); // ✅ Drop-down ke liye
        return "admin/add-candidate";  // 👈 Ye template load karega
    }
  @PostMapping("/add-candidate")
  public String addCandidate(@ModelAttribute Candidate candidate,
                             @RequestParam("logoFile") MultipartFile file,
                             RedirectAttributes ra) {

      try {
          if (!file.isEmpty()) {

              String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
              String uploadDir = "C:/Users/HP/OneDrive/Desktop/AI/uploads/";

              File uploadPath = new File(uploadDir);
              if (!uploadPath.exists()) {
                  uploadPath.mkdirs();
              }
              File saveFile = new File(uploadDir + fileName);
              file.transferTo(saveFile);
              candidate.setLogo(fileName);
          }

          candidateService.saveCandidate(candidate);
          ra.addFlashAttribute("successMessage", "Candidate added successfully!");

      } catch (Exception e) {
          e.printStackTrace();
          ra.addFlashAttribute("errorMessage", "Error uploading logo");
      }

      return "redirect:/admin/candidates";
  }


    @GetMapping("/verify-voters")
    public String showUnverifiedVoters(Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login";
        }

        List<Voter> pendingVoters = voterService.getUnverifiedVoters();
        model.addAttribute("pendingVoters", pendingVoters);
        return "admin/verify-voters";
    }

    @PostMapping("/verify-voters/{id}/approve")
    public String approveVoter(@PathVariable Long id, RedirectAttributes redirectAttributes,HttpSession session) {
        // check that admin is logged in
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login";
        }
        try {
            voterService.approveVoter(id);
            redirectAttributes.addFlashAttribute("success", "Voter approved successfully.");
        } catch (Exception e) {
            // log error
            redirectAttributes.addFlashAttribute("error", "Error approving voter: " + e.getMessage());
        }
        return "redirect:/admin/verify-voters";
    }



    @PostMapping("/verify-voters/{id}/reject")
    public String rejectVoter(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login";
        }

        voterService.deleteVoter(id);
        return "redirect:/admin/verify-voters";
    }

    @GetMapping("/result")
    public String showResults(Model model) {
        Election election = electionService.getLatestElection();

        if (election == null || election.getStatus() != Election.Status.COMPLETED) {
            model.addAttribute("message", "Results will be available after voting ends.");
            return "admin/result";
        }

        List<Candidate> results = candidateService
                .findByElectionOrderByVoteCountDesc(election.getId());

        boolean chainValid = blockchainService.verifyChain();

        model.addAttribute("results", results);
        model.addAttribute("chainValid", chainValid);
        model.addAttribute("election", election);
        return "admin/result";
    }


    @PostMapping("/admin/candidates/update")
    public String updateCandidate(@ModelAttribute Candidate candidate) {
        candidateService.updateCandidate(candidate);
        return "redirect:/admin/candidates";
    }


    @PostMapping("/candidates/delete/{id}")
    public String deleteCandidate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        candidateService.deleteCandidate(id);
        redirectAttributes.addFlashAttribute("successMessage", "Candidate deleted successfully!");
        return "redirect:/admin/candidates";
    }




    @GetMapping("/verify-chain")
    public String verifyChain(Model model) {
        boolean valid = blockchainService.verifyChain();
        List<Block> blocks = blockchainService.getAllBlocks();

        model.addAttribute("blocks", blocks);
        model.addAttribute("valid", valid);
        return "admin/verify-chain";
    }






















}



