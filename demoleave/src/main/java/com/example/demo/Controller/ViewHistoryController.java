package com.example.demo.Controller;

import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.LeaveApplicationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
@Controller
public class ViewHistoryController {
    @Autowired
    private LeaveApplicationService leaveApplicationService;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/{userID}/History")
    public String viewHistory(@PathVariable String userID, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        var userid = user.getId();
        var leaveApplications = leaveApplicationService.getLeaveApplicationForUser(userid);
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
}
