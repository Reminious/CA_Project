package com.example.demo.Service;

import com.example.demo.Entity.LeaveApplication;
import com.example.demo.Entity.LeaveRemainDTO;
import com.example.demo.Repository.LeaveApplicationRepository;
import com.example.demo.Repository.LeaveRemainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SubmissionService {
    @Autowired
    private LeaveRemainService leaveRemainService;
    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    public boolean checkTimeOverlapping(Integer UserID, LocalDate startDate, LocalDate endDate) {
        List<LeaveApplication> oldApplications = leaveApplicationRepository.findByUser_IdAndStatus(UserID, "Applied");
        if (!oldApplications.isEmpty()) {
            for (LeaveApplication oldApplication : oldApplications) {
                if (!oldApplication.getStartDate().isAfter(endDate) && !oldApplication.getEndDate().isBefore(startDate)) {
                    return true;
                }
                ;
            }
        }
        return false;
    }

    public boolean checkTime(LocalDate startDate, LocalDate endDate) {

        return ((isWorkingDay(startDate) && isWorkingDay(endDate)) && startDate.isBefore(endDate)) && (startDate.getYear() == endDate.getYear());
        //if startDate is later than endDate,
        //this statement will be more than 0,return false.
        //And the vacation should be in one year, if start and end year is not equal, it will return false.
    }

    public boolean checkAvailability(Integer userId, Integer Year, String leaveType, Integer daysDifference) {
        Integer leaveTime = 0;
        LeaveRemainDTO newLeave = leaveRemainService.getLeaveRemainByUserIDAndYear(userId, Year);
        switch (leaveType) {
            case "Annual":
//                System.out.println("选择了年假选项");
                leaveTime = newLeave.getAnnualRemain();
                break;
            case "Sick":
//                System.out.println("选择了病假选项");
                leaveTime = newLeave.getSickRemain();
                break;
            case "Compensation":
//                System.out.println("选择了补偿假选项");
                leaveTime = newLeave.getCompensationRemain();
                break;
            default:
//                System.out.println("无效选项");
        }
        return leaveTime > daysDifference;

    }

    public LeaveRemainDTO countRemain(Integer userId, String userName, Integer year, Integer applyTime, Integer annualRemain, Integer sickRemain, Integer compensationRemain, String leaveType) {
        LeaveRemainDTO newRemain = new LeaveRemainDTO(userId, userName, year, null, null, null);
        switch (leaveType) {
            case "Annual":
                newRemain.setCompensationRemain(compensationRemain);
                newRemain.setSickRemain(sickRemain);
                newRemain.setAnnualRemain(annualRemain - applyTime);
                break;
            case "Sick":
                newRemain.setCompensationRemain(compensationRemain);
                newRemain.setSickRemain(sickRemain - applyTime);
                newRemain.setAnnualRemain(annualRemain);
                break;
            case "Compensation":
                newRemain.setCompensationRemain(compensationRemain - applyTime);
                newRemain.setSickRemain(sickRemain);
                newRemain.setAnnualRemain(annualRemain);
                break;
            default:
                System.out.print(false);
        }
        return newRemain;
    }

    // 定义法定节假日列表
    private static final Map<String,MonthDay> publicHolidays = new HashMap<>();

    static {
        publicHolidays.put("New Year's Day",MonthDay.of(1, 1));
        publicHolidays.put("Spring Festival",MonthDay.of(2, 10));
        publicHolidays.put("Spring Festival2",MonthDay.of(2, 11));
        publicHolidays.put("Good Friday",MonthDay.of(3,29));
        publicHolidays.put("Hari Raya Puasa",MonthDay.of(4,10));
        publicHolidays.put("Labour Day",MonthDay.of(5,1));
        publicHolidays.put("Vesak Day",MonthDay.of(5,22));
        publicHolidays.put("Hari Raya Haji",MonthDay.of(6,17));
        publicHolidays.put("National Day",MonthDay.of(8,9));
        publicHolidays.put("Deepavali",MonthDay.of(10,31));
        publicHolidays.put("Christmas Day",MonthDay.of(12, 25));

    }

    // 判断是否为工作日
    public static boolean isWorkingDay(LocalDate date) {
        return date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY
                && !isHoliday(date);
    }

    public static boolean isHoliday(LocalDate date) {
        MonthDay monthDay = MonthDay.of(date.getMonth(), date.getDayOfMonth());
        return publicHolidays.containsValue(monthDay);
    }

    //
    // 计算两个日期之间的工作日天数
    public static Integer countWorkingDays(LocalDate startDate, LocalDate endDate) {
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
