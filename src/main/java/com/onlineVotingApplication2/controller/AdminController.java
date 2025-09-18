
package com.onlineVotingApplication2.controller;

import com.onlineVotingApplication2.entity.Candidate;
import com.onlineVotingApplication2.entity.User;
import com.onlineVotingApplication2.repository.CandidateRepository;
import com.onlineVotingApplication2.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")  // ✅ base path for all admin routes
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidateRepository candidateRepository; // ✅ now injected

    // Show login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "admin/login"; // ✅ templates/admin/login.html
    }

    // Handle login form
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {

        User admin = userRepository.findByUsername(username).orElse(null);

        if (admin != null &&
                "ADMIN".equals(admin.getRole()) &&
                admin.getPassword().equals(password)) {

            session.setAttribute("adminUser", admin);
            return "redirect:/admin/dashboard"; // ✅ correct redirect
        }

        model.addAttribute("error", "Invalid username or password");
        return "admin/login"; // ✅ stay on login page
    }

    // Dashboard page
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login";
        }

        // Example placeholders, replace with service layer calls
        model.addAttribute("candidates", java.util.Collections.emptyList());
        model.addAttribute("voters", java.util.Collections.emptyList());
        model.addAttribute("votesCast", 0);
        model.addAttribute("blocks", java.util.Collections.emptyList());

        return "admin/dashboard"; // ✅ templates/admin/dashboard.html
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

    // Show Add Candidate Form
  /*  @GetMapping("/add-candidate")
    public String showAddCandidateForm(Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("candidate", new Candidate());
        return "admin/add-candidate"; // ✅ must include "admin/"
    }
*/
    // Handle Candidate Save
  /*  @PostMapping("/add-candidate")
    public String saveCandidate(@ModelAttribute Candidate candidate,
                                HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login";
        }
        candidateRepository.save(candidate);
        return "redirect:/admin/dashboard"; // ✅ redirect after save
    }*/

    @PostMapping("/add-candidate")
    public String saveCandidate(@ModelAttribute Candidate candidate,Model model,
                                HttpSession session
//                                org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

    ){
                                if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login";
        }


        candidateRepository.save(candidate);
                                // Reset form + success message
//        model.addAttribute("candidate", new Candidate());
        model.addAttribute("successMessage", "�� Candidate added successfully!");
        return "admin/add-candidate"; // reload same page with message


        // ✅ Add success message
//        redirectAttributes.addFlashAttribute("successMessage", "Candidate saved successfully!");

//        return "redirect:/admin/dashboard";
    }

    ///   ///////////


    @GetMapping("/add-candidate")
    public String showAddCandidateForm(Model model) {
        model.addAttribute("candidate", new Candidate()); // empty form
        return "admin/add-candidate"; // matches templates/admin/add-candidate.html
    }

    // ✅ Handle Candidate Save
    /*@PostMapping("/add-candidate")
    public String saveCandidate(Candidate candidate, Model model) {
        candidateRepository.save(candidate);

        // Reset form + success message
        model.addAttribute("candidate", new Candidate());
        model.addAttribute("successMessage", "✅ Candidate added successfully!");

        return "admin/add-candidate"; // reload same page with message
    }
*/




}
