package com.example.demo.Service;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.AdminRepository;
import com.example.demo.Repository.LeaveApplicationRepository;
import com.example.demo.Repository.LeaveRemainRepository;
import com.example.demo.Repository.UserMapper;
import com.example.demo.Repository.UserRepository;

import jakarta.transaction.Transactional;

import com.example.demo.Entity.LeaveApplication;
import com.example.demo.Entity.LeaveRemain;
import com.example.demo.Entity.LeaveRemainDTO;
import com.example.demo.Entity.LeaveRemainId;
import com.example.demo.Entity.User;

@Transactional
@Service
public class UserService {
    @Autowired
    private UserMapper usermapper;
    public User authenticateUser(String name, String password) {
        return usermapper.findByNameAndPassword(name, password);
    }
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;
    @Autowired
    private LeaveRemainRepository leaveRemainRepository;
    public void submitLeaveApplication(LeaveApplication leaveApplication) {
        // Set additional fields before saving if needed
        leaveApplication.setStatus("Applied"); // Set default status to Pending 
        leaveApplication.setSubmitDate(Instant.now()); 
        // Set submit date to the current timestamp
        // Save the LeaveApplication entity to the database
        leaveApplicationRepository.save(leaveApplication);
    }

    
	}




