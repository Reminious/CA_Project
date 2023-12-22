package com.example.demo.Controller;
import com.example.demo.Entity.ManagerLeaveApplicationDTO;
import com.example.demo.Entity.User;
import com.example.demo.Service.ManagerLeaveApplicationService;
import com.example.demo.Service.ManagerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.ModelAndView;

@Controller
public class ManagerController {
    @Autowired
    ManagerService managerService;

    @Autowired
    private ManagerLeaveApplicationService managerleaveApplicationService;
    
    

    @GetMapping("/Manager")
    public String ManagerControl(){
        return "managerWelcome";
    }

//上面manager登录没问题了
    @GetMapping("/Manager/Leaves")
    public String showLeaveRemain(HttpSession session,Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "8") int size){
    	User user1 = (User) session.getAttribute("user");
    	Integer id=user1.getId();
    	//System.out.println("Manager ID: " + id);
    	Page<ManagerLeaveApplicationDTO> leavesPage=managerleaveApplicationService.getLeaveApplicationsByApproverAndManager(id,PageRequest.of(page,size));
        model.addAttribute("leavesPage",leavesPage);

        return "managerSubLeave";
    }
    
    
    @GetMapping("/Manager/Done")
    public String showDoneLeave(HttpSession session,Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "8") int size){
    	User user1 = (User) session.getAttribute("user");
    	Integer id=user1.getId();
    	//System.out.println("Manager ID: " + id);
    	Page<ManagerLeaveApplicationDTO> leavesPage=managerleaveApplicationService.getLeaveApplicationsByApproverAndManager(id,PageRequest.of(page,size));
        model.addAttribute("leavesPage",leavesPage);

        return "operateHistory";
    }
    
    
    @GetMapping("/operateLeave")
    public String EditForm(@RequestParam("applicationId") Integer Id,
    		//这个应该是Leaveapplication的id
                           Model model) {
        ManagerLeaveApplicationDTO leaveApplicationDTO = managerleaveApplicationService.getLeaveApplicationByApplicationId(Id);
        if(leaveApplicationDTO==null){
            return "redirect:/Manager/Leaves";
        }
        model.addAttribute("leaveApplicationDTO", leaveApplicationDTO);
        return "operateLeave";
    }

    @PostMapping("/operateLeave")
    public ModelAndView updateLeaveRemain(@ModelAttribute("leaveApplicationDTO") ManagerLeaveApplicationDTO leaveApplicationDTO) {
    	leaveApplicationDTO.setApproveDate();
        managerleaveApplicationService.updateLeaveApplication(leaveApplicationDTO);
        //System.out.println("Current ZoneId: " + leaveApplicationDTO.getApproveDate());
        
        //这里需要执行一个判断approved或rejected，并把通过的数据更新到leave_remain的操作
        managerleaveApplicationService.updateLeaveRemain(leaveApplicationDTO);
        
        return new ModelAndView("redirect:/Manager/Leaves");
    }


    @PostMapping("/Manager/operateLeave")
    //这个原来是userid需要调整一下，命名要对齐
    public String operateLeave(@ModelAttribute ManagerLeaveApplicationDTO leaveApplicationDTO) {
        try {
            managerService.operateLeave(leaveApplicationDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/Manager";
    }

}

