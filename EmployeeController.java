package com.example.demo.Controller;

import java.io.Console;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Service.LeaveRemainService;
import com.example.demo.Service.UserService;
import com.example.demo.Entity.LeaveApplication;
import com.example.demo.Entity.LeaveRemainDTO;
import com.example.demo.Entity.User;
import com.example.demo.Repository.LeaveApplicationRepository;
import com.example.demo.Repository.LeaveRemainRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class EmployeeController {
    @Autowired
    UserService employeeService;
    @Autowired
    LeaveRemainService leaveremainService;
    @Autowired
    private LeaveRemainRepository leaveRemainRepository;
    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;
    @GetMapping("/Employee")
    public String showLeaveForm(Model model,HttpSession session) {
    	User user =  (User) session.getAttribute("user");
    	
    	return "employee";
    }
    
    @PostMapping("/Employee/submitLeave")
	public String submitLeave(
								  @RequestParam LocalDate startDate,								  
								  @RequestParam LocalDate endDate,
								  @RequestParam String leaveType,
								  @RequestParam String reason,
								  @RequestParam String contact_details,
								  @RequestParam String work_dissemination,
								  HttpSession session, Model model
								 ) {
		
		  User user = (User) session.getAttribute("user"); 
		  model.addAttribute("userId",user.getId()); 
		  Integer userId=user.getId();
		  String userName=user.getName();
		  //for a user, if there's already application for vacation
		  //(means the status is "Applied"),
		  //then user cannot apply for vacation until status is changed.
		  
		  Boolean applicationOK=false;
		  int year = startDate.getYear();
		  Integer daysDifference = (int) ChronoUnit.DAYS.between(startDate, endDate)+1;
		  //Check user's application
		  //1.check select time:
		  //2.check whether time is enough, including compensation time.
		  if(!checkTimeOverlapping(userId, startDate,  endDate)&&checkTime(startDate,endDate)&&checkAvailability(userId,year,leaveType,daysDifference)) {
			  applicationOK=true;
		  }
		  //if checking is done, add this application to our database.
		  if(applicationOK) {
			  LeaveApplication newleave=new LeaveApplication(); 
			  newleave.setLeaveType(leaveType);
			  newleave.setStartDate(startDate);
			  newleave.setEndDate(endDate);
			  newleave.setUserID(userId);
			  newleave.setReason(reason);
			  newleave.setWork_dissemination(work_dissemination);
			  newleave.setContact_details(contact_details);
			  employeeService.submitLeaveApplication(newleave);
			  LeaveRemainDTO ToEditLeave=leaveremainService.getLeaveRemainByUserIDAndYear(userId,year);
			  Integer annualRemain=ToEditLeave.getAnnualRemain();
			  Integer sickRemain=ToEditLeave.getSickRemain();
			  Integer compensationRemain=ToEditLeave.getCompensationRemain();
			  //When apply annual vacation, we need to check everyday in apply : if the leave period is <= 14 calendar days, weekends / public holidays are excluded.
			  //Otherwise, weekends / public holidays are included	
			  //1.weekend?
			  //2.public holiday?	  
			  if(daysDifference <= 14&&leaveType.equals("annual")) {
				  daysDifference=countWorkingDays(startDate,endDate);
			  }
			  LeaveRemainDTO updateremain=CountRemain(userId,userName,year,daysDifference,annualRemain, sickRemain, compensationRemain,leaveType);
			  leaveremainService.updateLeaveRemain(updateremain);
			  return "redirect:/Employee";
		  }
		  else {
			  return "error";
		  }

    }
    private boolean checkTimeOverlapping(Integer UserID,LocalDate startDate, LocalDate endDate) {
    	boolean isOverlapping=false;
    	LeaveApplication oldApplication=leaveApplicationRepository.findByUserIDAndStatus(UserID, "Applied");
    	if(oldApplication!=null) {
    		if(oldApplication.getStartDate().isAfter(endDate)||oldApplication.getEndDate().isBefore(startDate)) {
    			isOverlapping=false;
    		}
    		else {
    			isOverlapping=true;
    		}
    	}
    	return isOverlapping;
    }
	private boolean checkTime(LocalDate startDate, LocalDate endDate) {
		
		return ((isWorkingDay(startDate)||isWorkingDay(endDate))&&startDate.compareTo(endDate) < 0)&&(startDate.getYear()==endDate.getYear());
		//if startDate is later than endDate, 
		//this statement will be more than 0,return false.
		//And the vacation should be in one year, if start and end year is not equal, it will return false.
	}
	
	private boolean checkAvailability(Integer userId,Integer Year,String leaveType,Integer daysDifference) {
		Integer leaveTime=0;
			LeaveRemainDTO newleave=leaveremainService.getLeaveRemainByUserIDAndYear(userId,Year);
			switch (leaveType) {
            case "annual":
                System.out.println("选择了年假选项");
                leaveTime=newleave.getAnnualRemain();
                break;
            case "sick":
                System.out.println("选择了病假选项");
                leaveTime=newleave.getSickRemain();
                break;
            case "compensation":
                System.out.println("选择了补偿假选项");
                leaveTime=newleave.getCompensationRemain();
                break;
            default:
                System.out.println("无效选项");}
		if(leaveTime>daysDifference) {
			return true;
		}
		else {
			return false;
		}

	}

    private LeaveRemainDTO CountRemain(Integer userId,String userName,Integer year,Integer applyTime,Integer annualRemain,Integer sickRemain,Integer compensationRemain,String leaveType) {
    	LeaveRemainDTO newRemain=new LeaveRemainDTO(userId, userName, year, null, null, null);
    	switch (leaveType) {
    	case "annual":
    		newRemain.setCompensationRemain(compensationRemain);
    		newRemain.setSickRemain(sickRemain);
    		newRemain.setAnnualRemain(annualRemain-applyTime);
            break;
    	case "sick":
    		newRemain.setCompensationRemain(compensationRemain);
    		newRemain.setSickRemain(sickRemain-applyTime);
    		newRemain.setAnnualRemain(annualRemain);
    		break;
    	case "compensation":
    		newRemain.setCompensationRemain(compensationRemain-applyTime);
    		newRemain.setSickRemain(sickRemain);
    		newRemain.setAnnualRemain(annualRemain);
    		break;
    	default:
    		System.out.print(false);
    	}
    	return newRemain;
    }
    // 定义法定节假日列表
    private static final Map<LocalDate, String> publicHolidays = new HashMap<>();
    static {
        publicHolidays.put(LocalDate.of(2023, 12, 25), "Christmas Day");

    }

    // 判断是否为工作日
    public static boolean isWorkingDay(LocalDate date) {
        return date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY
                && !publicHolidays.containsKey(date);
    }

    // 
    // 计算两个日期之间的工作日天数
    private static Integer countWorkingDays(LocalDate startDate, LocalDate endDate) {
        Integer workingDays = 0;
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            if (isWorkingDay(currentDate)) {
                workingDays++;
            }
            currentDate = currentDate.plusDays(1);
        }
        return workingDays;
    }
}

