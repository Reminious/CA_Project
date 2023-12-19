package com.example.demo.Controller;
import com.example.demo.Entity.LeaveRemainDTO;
import com.example.demo.Entity.User;
import com.example.demo.Service.LeaveRemainService;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.demo.Service.AdminService;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    UserService userService;
    @Autowired
    private LeaveRemainService leaveRemainService;

    @GetMapping("/Admin")
    public String adminControl(HttpSession session, Model model){
        return "adminWelcome";
    }

    @GetMapping("/Admin/Leaves")
    public String showLeaveRemain(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "8") int size){
        Page<LeaveRemainDTO> leavesPage=leaveRemainService.GetAllLeaveRemains(PageRequest.of(page,size));
        model.addAttribute("leavesPage",leavesPage);
        return "adminLeaveManagement";
    }

    @GetMapping("/Admin/editLeave")
    public String editLeaveForm(@RequestParam("userID") Integer userID,
                                @RequestParam("year") Integer year,
                                Model model) {
        LeaveRemainDTO leaveRemainDTO = leaveRemainService.getLeaveRemainByUserIDAndYear(userID, year);
        if(leaveRemainDTO==null){
            return "redirect:/Admin/Leaves";
        }
        model.addAttribute("leaveRemainDTO", leaveRemainDTO);
        return "adminEditLeave";
    }

    @PostMapping("/Admin/editLeave")
    public ModelAndView updateLeaveRemain(@ModelAttribute("leaveRemainDTO") LeaveRemainDTO leaveRemainDTO) {
        leaveRemainService.updateLeaveRemain(leaveRemainDTO);
        return new ModelAndView("redirect:/Admin/Leaves");
    }

    @GetMapping("/Admin/searchLeaves")
    public String searchLeaves(Model model,
                              @RequestParam(required = false) String username,
                              @RequestParam(required = false) Integer userId,
                              @RequestParam(required = false) Integer year,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "8") int size) {
        Page<LeaveRemainDTO> leavesPage = leaveRemainService.searchLeave(username, userId, year, PageRequest.of(page, size));
        model.addAttribute("leavesPage", leavesPage);
        return "adminLeaveManagement";
    }

    @GetMapping("/Admin/deleteLeave")
    public String deleteLeave(@RequestParam("userID") Integer userID,
                              @RequestParam("year") Integer year) {
        leaveRemainService.deleteLeave(userID, year);
        return "redirect:/Admin/Leaves";
    }

    @PostMapping("/Admin/addLeave")
    public String addLeave(@RequestParam Integer userId,
                           @RequestParam Integer year,
                           @RequestParam Integer AnnualRemain,
                           @RequestParam Integer SickRemain,
                           @RequestParam Integer CompensationRemain) {
        adminService.addLeave(userId, year, AnnualRemain, SickRemain, CompensationRemain);
        return "redirect:/Admin/Leaves";
    }

    @GetMapping("/Admin/searchUsers")
    public String searchUsers(Model model,
                              @RequestParam(required = false) String username,
                              @RequestParam(required = false) Integer userId,
                              @RequestParam(required = false) String jobTitle,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "8") int size) {
        Page<User> usersPage = userService.searchUsers(username, userId, jobTitle, PageRequest.of(page, size));
        model.addAttribute("usersPage", usersPage);
        return "adminUserManagement";
    }

    @GetMapping("/Admin/deleteUser")
    public String deleteUser(@RequestParam("id") Integer id) {
        userService.deleteUser(id);
        return "redirect:/Admin/Users";
    }

    @PostMapping("/Admin/addUser")
    public String addUser(@RequestParam String name,
                          @RequestParam String password,
                          @RequestParam String jobTitle,
                          @RequestParam(required = false) Integer supervisor) {
        User newUser = new User();
        newUser.setName(name);
        newUser.setPassword(password);
        newUser.setJobTitle(jobTitle);
        newUser.setSupervisor(supervisor);
        userService.addUser(newUser);
        return "redirect:/Admin/Users";
    }

    @GetMapping("/Admin/editUser")
    public String editUserForm(@RequestParam("id") Integer id,
                               Model model) {
        User user = userService.getUserById(id);
        if(user==null){
            return "redirect:/Admin/Users";
        }
        model.addAttribute("user", user);
        return "adminEditUser";
    }

    @PostMapping("/Admin/editUser")
    public ModelAndView updateUser(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return new ModelAndView("redirect:/Admin/Users");
    }

    @GetMapping("/Admin/Users")
    public String showUsers(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "8") int size){
        Page<User> usersPage=userService.getAllUsers(PageRequest.of(page,size));
        model.addAttribute("usersPage",usersPage);
        return "adminUserManagement";
    }
}

