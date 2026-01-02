package in.vicky.main.mailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import in.vicky.main.OvsApplication;
import in.vicky.main.entities.User;
import in.vicky.main.repository.UserRepository;

@Service
public class UserService {

    private final OvsApplication ovsApplication;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    UserService(OvsApplication ovsApplication) {
        this.ovsApplication = ovsApplication;
    }

    public void registerUser(User user) {
        userRepository.save(user);
        sendSuccessEmail(user.getEmail());
    }

    private void sendSuccessEmail(String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("iamdon8898@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Registration Successful");
        message.setText("Congratulations! You have successfully registered for the Online Voting System.");
        mailSender.send(message);
    }
    
    
    public boolean recoverPassword(String email) {
        User user = userRepository.findByEmail(email);
        
        if (user != null) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Password Recovery - OVS");
            message.setText("Your password for the Online Voting System is: " + user.getPassword());
            mailSender.send(message);
            return true;
        }
        return false;
    }
    
}