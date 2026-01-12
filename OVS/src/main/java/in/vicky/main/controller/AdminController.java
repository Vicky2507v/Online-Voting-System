package in.vicky.main.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.vicky.main.repository.CandidateRepository;
import in.vicky.main.entities.Candidate;
import in.vicky.main.entities.User;
import in.vicky.main.repository.UserRepository;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CandidateRepository candidateRepository;

    
    @GetMapping("/users")
    public String viewUsers(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login"; // user-login
        }
        model.addAttribute("users", userRepository.findAll());
        return "admin-users";
    }
    

    // Show Add Candidate Form
    @GetMapping("/add-candidate")
    public String showCandidateForm(HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login"; 
        }
        return "add-candidate"; 
    }

    // Process Candidate Addition with Photo
    @PostMapping("/add-candidate")
    public String addCandidate(@RequestParam String name, 
                               @RequestParam String party, 
                               @RequestParam("photo") MultipartFile file) throws IOException {
        
        String fileName = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/images/" + fileName);
        Files.write(path, file.getBytes());

        Candidate candidate = new Candidate();
        candidate.setCandidateName(name);
        candidate.setPartyName(party);
        candidate.setPhotoPath("/images/" + fileName);
        
        candidateRepository.save(candidate);
        return "redirect:/admin/users";
    }
    
    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        // 1. Security Check
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        try {
            // 2. Delete the user
            userRepository.deleteById(id);
            
            // 3. Add success message
            redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting user: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }
    
    
    @GetMapping("/results")
    public String viewResults(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }
        
        List<Candidate> candidates = candidateRepository.findAll();
        
        // Calculate total votes using streams
        int totalVotes = candidates.stream()
                                   .mapToInt(Candidate::getVoteCount)
                                   .sum();
        
        model.addAttribute("candidates", candidates);
        model.addAttribute("totalVotes", totalVotes);
        
        return "admin-results";
    }
    
    
    @GetMapping("/reset-election")
    public String resetElection(HttpSession session, RedirectAttributes redirectAttributes) {
        // 1. Security Check
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        try {
            
            List<Candidate> candidates = candidateRepository.findAll();
            for (Candidate c : candidates) {
                c.setVoteCount(0);
            }
            candidateRepository.saveAll(candidates);

            List<User> users = userRepository.findAll();
            for (User u : users) {
                u.setHasVoted(false);
            }
            userRepository.saveAll(users);

            redirectAttributes.addFlashAttribute("success", "Election has been reset! All votes are now 0.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Reset failed: " + e.getMessage());
        }

        return "redirect:/admin/results";
    }
    

}
