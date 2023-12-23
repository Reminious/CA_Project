package com.example.demo.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
@Service
public class EmailService {
    @Value("${spring.mail.username}")
    private String officialEmail;
    @Autowired
    private JavaMailSender javaMailSender;
    public void sendEmail(String adminEmail, String userID) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(officialEmail);
        msg.setTo(adminEmail);
        msg.setSubject("Password Reset Request");
        msg.setText("User with ID " + userID + " has requested to reset password. Please reset password for this user.");
        javaMailSender.send(msg);
    }

    public void sendLeaveEmail(String managerEmail, String userID) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(officialEmail);
        msg.setTo(managerEmail);
        msg.setSubject("Leave Application Request");
        msg.setText("User with ID " + userID + " has requested to apply for leave. Please approve or reject this leave application.");
        javaMailSender.send(msg);
    }
}
