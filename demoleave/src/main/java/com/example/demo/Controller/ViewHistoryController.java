package com.example.demo.Controller;
import com.example.demo.Entity.LeaveApplication;
import com.example.demo.Entity.User;
import com.example.demo.Service.LeaveApplicationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
@Controller
public class ViewHistoryController {
    @Autowired
    private LeaveApplicationService leaveApplicationService;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/{jobTitle}/{userID}/History")
    public String viewHistory(@PathVariable String jobTitle,@PathVariable String userID, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        var userid = user.getId();
        var userjobtitle = user.getJobTitle();
        var leaveApplications = leaveApplicationService.getLeaveApplicationForUser(userid);
        model.addAttribute("leaveApplications", leaveApplications);
        if ("Manager".equals(userjobtitle))
        	return "managerHistory";
        else return "history";
    }

    @GetMapping("/{jobTitle}/{userID}/History/cancel/{applicationId}")
    public String cancelApplication(@PathVariable String jobTitle,@PathVariable String userID,@PathVariable Integer applicationId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        leaveApplicationService.deleteLeaveApplication(applicationId);
        return "redirect:/{jobTitle}/{userID}/History";
    }

    @GetMapping("/{jobTitle}/{userID}/History/{applicationID}/update")
    public String modifyApplication(@PathVariable String jobTitle,@PathVariable String userID, @PathVariable String applicationID, Model model){
        Integer ID=Integer.parseInt(applicationID);
        LeaveApplication leaveApplication = leaveApplicationService.getLeaveApplicationById(ID);
        model.addAttribute("leaveApplication", leaveApplication);
        return "updateApplication";
    }

    @PostMapping("/{jobTitle}/{userID}/History/update/{applicationID}")
    public String updateApplication(@PathVariable String jobTitle, @PathVariable String userID, @PathVariable Integer applicationID, @ModelAttribute LeaveApplication updatedApplication){
        leaveApplicationService.updateLeaveApplication(applicationID, updatedApplication);
        return "adminWelcome";
    }
}
