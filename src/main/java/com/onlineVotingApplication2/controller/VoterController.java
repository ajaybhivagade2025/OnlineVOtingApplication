package com.onlineVotingApplication2.controller;

import com.onlineVotingApplication2.entity.User;
import com.onlineVotingApplication2.entity.Voter;
import com.onlineVotingApplication2.repository.UserRepository;
import com.onlineVotingApplication2.repository.VoterRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/voter")
public class VoterController {

    private final UserRepository userRepository;

   @Autowired
   private  VoterRepository voterRepository;

    public VoterController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Show voter registration form
@GetMapping("/register")
public String showRegisterPage(Model model) {
    model.addAttribute("voter", new Voter()); // ✅ send an empty Voter object
    return "voter/voter-register"; // ✅ templates/voter/voter-register.html
}

    @PostMapping("/register")
public String processRegistration(Voter voter, Model model) {
    // Default values for a new voter
    voter.setName(voter.getName());
    voter.setMobile(voter.getMobile());
    voter.setEmail(voter.getEmail());
    voter.setHasVoted(false);
    voter.setVerified(false);

    voterRepository.save(voter);

    model.addAttribute("success", "Registration successful. Please wait for verification.");
    model.addAttribute("voter", new User()); // reset form
    return "voter/voter-register"; // stay on same page after registration
}

// inside VoterController.java

    // Show voter login form
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("voter", new User()); // empty user object
        return "voter/voter-login"; // looks for templates/voter/voter-login.html
    }

    // Process voter login
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        // Try to find voter by username
        User voter = userRepository.findByUsername(username).orElse(null);

        if (voter != null && "VOTER".equals(voter.getRole()) && voter.getPassword().equals(password)) {
            if (!voter.isVerified()) {
                model.addAttribute("error", "Your account is not verified yet. Please wait for admin approval.");
                return "voter/voter-login";
            }
            session.setAttribute("voterUser", voter);
            return "redirect:/voter/dashboard"; // ✅ you will create voter dashboard page
        }

        model.addAttribute("error", "Invalid username or password");
        return "voter/voter-login";
    }

    // Show voter dashboard (after login)
    @GetMapping("/dashboard")
    public String voterDashboard(HttpSession session) {
        if (session.getAttribute("voterUser") == null) {
            return "redirect:/voter/login";
        }
        return "voter/voter-dashboard"; // templates/voter/voter-dashboard.html
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/voter/login";
    }


}
