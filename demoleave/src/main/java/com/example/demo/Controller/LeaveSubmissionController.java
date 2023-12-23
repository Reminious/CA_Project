package com.example.demo.Controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.example.demo.Service.EmailService;
import com.example.demo.Service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Service.LeaveRemainService;
import com.example.demo.Service.UserService;
import com.example.demo.Entity.LeaveApplication;
import com.example.demo.Entity.LeaveRemainDTO;
import com.example.demo.Entity.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class LeaveSubmissionController {
    @Autowired
    UserService userService;
    @Autowired
    LeaveRemainService leaveremainService;
    @Autowired
    SubmissionService submissionService;
    @Autowired
    EmailService emailService;

    @GetMapping("{jobTitle}/{userID}/leaveSubmission")
    public String showLeaveForm(@PathVariable String jobTitle, @PathVariable String userID, HttpSession session) {
        User user = (User) session.getAttribute("user");
        return "leaveSubmission";
    }

    @PostMapping("{jobTitle}/{userID}/submitLeave")
    public String submitLeave(
            @PathVariable String jobTitle,
            @PathVariable String userID,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam String leaveType,
            @RequestParam String reason,
            @RequestParam String contact_details,
            @RequestParam String work_dissemination,
            HttpSession session, Model model
    ) {
        String managerEmail = "meta0000ff@gmail.com";
        User user = (User) session.getAttribute("user");
        model.addAttribute("userId", user.getId());
        Integer userId = user.getId();
        String userName = user.getName();
        String s=jobTitle+"Welcome";
        //for a user, if there's already application for vacation
        //(means the status is "Applied"),
        //then user cannot apply for vacation until status is changed.
        boolean applicationOK = false;
        int year = startDate.getYear();
        Integer daysDifference = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
        //Check user's application
        //1.check select time:
        //2.check whether time is enough, including compensation time.
        if (!submissionService.checkTimeOverlapping(userId, startDate, endDate) && submissionService.checkTime(startDate, endDate) && submissionService.checkAvailability(userId, year, leaveType, daysDifference)) {
            applicationOK = true;
        }
        //if checking is done, add this application to our database.
        if (applicationOK) {
            LeaveApplication newLeave = new LeaveApplication();
            newLeave.setLeaveType(leaveType);
            newLeave.setStartDate(startDate);
            newLeave.setEndDate(endDate);
            newLeave.setUser(user);
            newLeave.setReason(reason);
            newLeave.setWorkDissemination(work_dissemination);
            newLeave.setContactDetails(contact_details);
            newLeave.setApproverID(user.getSupervisor());
            userService.submitLeaveApplication(newLeave);
            LeaveRemainDTO toEditLeave = leaveremainService.getLeaveRemainByUserIDAndYear(userId, year);
            Integer annualRemain = toEditLeave.getAnnualRemain();
            Integer sickRemain = toEditLeave.getSickRemain();
            Integer compensationRemain = toEditLeave.getCompensationRemain();
            //When apply annual vacation, we need to check everyday in apply : if the leave period is <= 14 calendar days, weekends / public holidays are excluded.
            //Otherwise, weekends / public holidays are included
            //1.weekend?
            //2.public holiday?
            if (daysDifference <= 14 && leaveType.equals("annual")) {
                daysDifference = SubmissionService.countWorkingDays(startDate, endDate);
            }
            LeaveRemainDTO updateRemain = submissionService.countRemain(userId, userName, year, daysDifference, annualRemain, sickRemain, compensationRemain, leaveType);
            leaveremainService.updateLeaveRemain(updateRemain);
            model.addAttribute("ApplicationStatus", "Your application has been submitted successfully.");
            emailService.sendLeaveEmail(managerEmail, userID);
        } else {
			model.addAttribute("ApplicationStatus", "Please check your application's start and end date.");
        }
        return s;

    }




}