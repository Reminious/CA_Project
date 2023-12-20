package com.example.demo.Controller;

import com.example.demo.Entity.LeaveApplication;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.LeaveApplicationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.demo.Entity.LeaveApplicationDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
@Controller
public class ViewHistoryController {
    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @Autowired
    private UserRepository userRepository;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/{userID}/History")
    public String viewHistory(@PathVariable String userID, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        var userid = user.getId();
        var leaveApplications = leaveApplicationService.findByUser_Id(userid).stream().map(this::convertToDTO).collect(Collectors.toList());
        model.addAttribute("leaveApplications", leaveApplications);
        return "history";
    }

    @PostMapping("/{userID}/History/cancel")
    public String cancelApplication(@PathVariable String userID, HttpSession session, Model model, @RequestParam Integer applicationId) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        leaveApplicationService.deleteLeaveApplication(applicationId);
        return "redirect:/{userID}/History";
    }

    private LeaveApplicationDTO convertToDTO(LeaveApplication leaveApplication){
        LeaveApplicationDTO leaveApplicationDTO = new LeaveApplicationDTO();
        leaveApplicationDTO.setId(leaveApplication.getId());
        leaveApplicationDTO.setUsername(leaveApplication.getUser().getName());
        leaveApplicationDTO.setLeaveType(leaveApplication.getLeaveType());
        leaveApplicationDTO.setStartDate(formatLocalDate(leaveApplication.getStartDate()));
        leaveApplicationDTO.setEndDate(formatLocalDate(leaveApplication.getEndDate()));
        leaveApplicationDTO.setSubmitDate(formatInstant(leaveApplication.getSubmitDate()));
        leaveApplicationDTO.setReason(leaveApplication.getReason());
        leaveApplicationDTO.setStatus(leaveApplication.getStatus());
        leaveApplicationDTO.setApproverID(leaveApplication.getApproverID()==null?null:leaveApplication.getApproverID());
        leaveApplicationDTO.setApproveDate(formatInstant(leaveApplication.getApproveDate())==null?null:formatInstant(leaveApplication.getApproveDate()));
        leaveApplicationDTO.setComment(leaveApplication.getComment()==null?null:leaveApplication.getComment());
        if (leaveApplication.getApproverID() == null) {
            leaveApplicationDTO.setApproverName(null);
        } else {
            leaveApplicationDTO.setApproverName(userRepository.findById(leaveApplication.getApproverID()).map(User::getName).orElse(null));
        }
        return leaveApplicationDTO;
    }

    private String formatLocalDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return localDate.format(formatter);
        }
    }

    private String formatInstant(Instant instant) {
        if (instant == null) {
            return null;
        } else {
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return localDateTime.format(formatter);
        }
    }

}
