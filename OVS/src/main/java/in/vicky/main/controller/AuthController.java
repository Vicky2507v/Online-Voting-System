package in.vicky.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.vicky.main.entities.Candidate;
import in.vicky.main.entities.User;
import in.vicky.main.repository.CandidateRepository;
import in.vicky.main.mailService.UserService;
import jakarta.servlet.http.HttpSession;
import in.vicky.main.repository.UserRepository;

@Controller
public class AuthController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CandidateRepository candidateRepository;
	@Autowired
	private UserService userService;

	// Show Registration Page
	@GetMapping("/register")
	public String showRegistrationPage() {
		return "register"; 
	}

	// Process Registration
	@PostMapping("/register")
	public String processRegistration(@ModelAttribute User user, Model model) {
		try {
			userService.registerUser(user);
			model.addAttribute("message", "Registration successful! Please login.");
			return "login";
		} catch (Exception e) {
			model.addAttribute("error", "Registration failed: " + e.getMessage());
			return "register";
		}
	}

	// Show Login Page
	@GetMapping("/login")
	public String showLoginPage() {
		return "login";
	}

	// Single Unified Process Login
	@PostMapping("/login")
	public String processLogin(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
	    // 1. Admin
	    if ("admin@gmail.com".equals(email) && "admin123".equals(password)) {
	        session.setAttribute("admin", "true");
	        return "redirect:/admin/users";
	    }

	    // 2. User
	    User user = userRepository.findByEmail(email); // Use findByEmail
	    if (user != null && user.getPassword().equals(password)) {
	        session.setAttribute("user", user);
	        return "redirect:/dashboard"; // REDIRECT is key here
	    }

	    model.addAttribute("error", "Invalid Credentials");
	    return "login";
	}

	// Show Forgot Password Page
	@GetMapping("/forgot-password")
	public String showForgotPasswordPage() {
		return "forgot-password";
	}

	// Process Forgot Password
	@PostMapping("/forgot-password")
	public String processForgotPassword(@RequestParam String email, Model model) {
		boolean sent = userService.recoverPassword(email);
		if (sent) {
			model.addAttribute("message", "Password has been sent to your email.");
		} else {
			model.addAttribute("error", "Email not found in our records.");
		}
		return "forgot-password";
	}

	@GetMapping("/dashboard")
	public String showDashboard(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return "redirect:/login";
		}

		model.addAttribute("user", user);
		model.addAttribute("candidates", candidateRepository.findAll());
		return "dashboard";
	}

	@PostMapping("/vote")
	public String castVote(@RequestParam Long candidateId, HttpSession session, RedirectAttributes redirectAttributes) {
	    User loggedInUser = (User) session.getAttribute("user");

	    if (loggedInUser == null) {
	        return "redirect:/login";
	    }

	    User user = userRepository.findById(loggedInUser.getId()).get();

	    if (user.isHasVoted()) {
	        redirectAttributes.addFlashAttribute("error", "You have already cast your vote!");
	    } else {
	        Candidate candidate = candidateRepository.findById(candidateId).get();
	        candidate.setVoteCount(candidate.getVoteCount() + 1);
	        candidateRepository.save(candidate);

	        user.setHasVoted(true);
	        userRepository.save(user);

	        session.setAttribute("user", user); 
	        redirectAttributes.addFlashAttribute("success", "Thank you! Your vote for " + candidate.getCandidateName() + " has been recorded.");
	    }

	    return "redirect:/dashboard";
	}
	
}