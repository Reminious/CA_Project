package com.example.demo.Service;

import com.example.demo.Entity.LeaveRemainId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.Entity.ManagerLeaveApplication;
import com.example.demo.Entity.ManagerLeaveApplicationDTO;
import com.example.demo.Entity.LeaveRemain;
import com.example.demo.Entity.LeaveRemainDTO;
import com.example.demo.Repository.ManagerLeaveApplicationRepository;
import com.example.demo.Repository.LeaveRemainRepository;
import com.example.demo.Repository.ManagerRepository;
import com.example.demo.Repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.Entity.User;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class ManagerLeaveApplicationService {
    @Autowired
    private ManagerLeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private LeaveRemainRepository leaveRemainRepository;

    @Autowired
    private ManagerRepository managerRepository;
    
    public Page<ManagerLeaveApplicationDTO> getLeaveApplicationsByApproverAndManager(Integer managerId, Pageable pageable) {

        Page<ManagerLeaveApplication> leaveApplicationPage = leaveApplicationRepository.findByApproverID(managerId, pageable);
        return leaveApplicationPage.map(this::convertToLeaveApplicationDTO);
    }

    public Page<ManagerLeaveApplicationDTO> getAppliedLeaveApplicationsByApproverAndManager(Integer managerId, Pageable pageable) {

        Page<ManagerLeaveApplication> leaveApplicationPage = leaveApplicationRepository.findByApproverIDAndStatus(managerId,"Applied", pageable);
        return leaveApplicationPage.map(this::convertToLeaveApplicationDTO);
    }

    public Page<ManagerLeaveApplicationDTO> getNotAppliedLeaveApplicationsByApproverAndManager(Integer managerId, Pageable pageable) {

        Page<ManagerLeaveApplication> leaveApplicationPage = leaveApplicationRepository.findByApproverIDAndStatusNot(managerId,"Applied", pageable);
        return leaveApplicationPage.map(this::convertToLeaveApplicationDTO);
    }
    
    
    public ManagerLeaveApplicationDTO getLeaveApplicationByApplicationId(Integer id) {
    	ManagerLeaveApplication leaveApplication = leaveApplicationRepository.findByApplicationId(id)
                .orElseThrow(() -> new EntityNotFoundException("Leave application not found with id: " + id));

        return convertToLeaveApplicationDTO(leaveApplication);
    }


    
    public void updateLeaveApplication(ManagerLeaveApplicationDTO leaveApplicationDTO) {
        Integer leaveApplicationId = leaveApplicationDTO.getApplicationId();
        ManagerLeaveApplication leaveApplication = leaveApplicationRepository.findByApplicationId(leaveApplicationId).orElse(null);
        if (leaveApplication != null) {
        	leaveApplication.setComment(leaveApplicationDTO.getComment());
            leaveApplication.setStatus(leaveApplicationDTO.getStatus());
            // 添加当前时间
            leaveApplication.setApproveDate(LocalDateTime.now());
            leaveApplicationRepository.save(leaveApplication);
            
        }
        
    }
    
    public void updateLeaveRemain(ManagerLeaveApplicationDTO leaveApplicationDTO) {
        Integer leaveApplicationId = leaveApplicationDTO.getApplicationId();
        ManagerLeaveApplication leaveApplication = leaveApplicationRepository.findByApplicationId(leaveApplicationId).orElse(null);

        if (leaveApplication != null && "Rejected".equals(leaveApplicationDTO.getStatus())) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy.M.d");
            String startDateStr = inputFormat.format(leaveApplicationDTO.getStartDate());
            String endDateStr = inputFormat.format(leaveApplicationDTO.getEndDate());

            try {
                Date startDate = new java.sql.Date(inputFormat.parse(startDateStr).getTime());
                Date endDate = new java.sql.Date(inputFormat.parse(endDateStr).getTime());

                Calendar startCalendar = Calendar.getInstance();
                startCalendar.setTime(startDate);

                Calendar endCalendar = Calendar.getInstance();
                endCalendar.setTime(endDate);

                while (startCalendar.get(Calendar.YEAR) <= endCalendar.get(Calendar.YEAR)) {
                	System.out.println("start: " + startCalendar.get(Calendar.YEAR));
                	System.out.println("end: " + endCalendar.get(Calendar.YEAR));
                    int currentYear = startCalendar.get(Calendar.YEAR);

                    LeaveRemainId leaveRemainId = new LeaveRemainId(leaveApplicationDTO.getUserID(), currentYear);
                    LeaveRemain leaveRemain = leaveRemainRepository.findById(leaveRemainId).orElse(null);

                    long diffInMillies = Math.abs(endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis());
                    long daysDifference = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                    System.out.println("daysDifference: " + daysDifference);
                    int theDifference = 0;

                    switch (leaveApplicationDTO.getLeaveType()) {
                        case "Sick":
                            theDifference = leaveRemain.getSickRemain() + (int) daysDifference;
                            System.out.println("theDifference: " + theDifference);
                            leaveRemain.setSickRemain(theDifference);
                            break;
                        case "Annual":
                            theDifference = leaveRemain.getAnnualRemain() + (int) daysDifference;
                            leaveRemain.setAnnualRemain(theDifference);
                            break;
                        case "Compensation":
                            theDifference = leaveRemain.getCompensationRemain() + (int) daysDifference;
                            leaveRemain.setCompensationRemain(theDifference);
                            break;
                        // Add other leave types as needed

                        default:
                            // Handle unknown leave types
                            break;
                    }

                    leaveRemainRepository.save(leaveRemain);

                    // Move to the next year
                    startCalendar.add(Calendar.YEAR, 1);

                    if (startCalendar.get(Calendar.YEAR) > endCalendar.get(Calendar.YEAR)) {
                        // 如果已经超过结束日期，提前终止循环
                        break;
                    }
                }
            } catch (ParseException e) {
                // 处理ParseException异常，例如打印错误信息或采取其他适当的措施
                e.printStackTrace();
            }
        }
    }

    

    public ManagerLeaveApplicationDTO convertToLeaveApplicationDTO(ManagerLeaveApplication leaveApplication) {
    	ManagerLeaveApplicationDTO leaveApplicationDTO = new ManagerLeaveApplicationDTO(leaveApplication.getApplicationId(), leaveApplication.getApproverID());
        leaveApplicationDTO.setUserID(leaveApplication.getUser().getId());
        leaveApplicationDTO.setUserName(leaveApplication.getUser().getName());
        leaveApplicationDTO.setReason(leaveApplication.getReason());
        leaveApplicationDTO.setLeaveType(leaveApplication.getLeaveType());
        leaveApplicationDTO.setStartDate(leaveApplication.getStartDate());
        leaveApplicationDTO.setEndDate(leaveApplication.getEndDate());
        leaveApplicationDTO.setSubmitDate(leaveApplication.getSubmitDate());
        leaveApplicationDTO.setApproverID(leaveApplication.getApproverID());
        leaveApplicationDTO.setComment(leaveApplication.getComment());
        leaveApplicationDTO.setStatus(leaveApplication.getStatus());
        leaveApplicationDTO.setApproveDate(leaveApplication.getApproveDate());
        leaveApplicationDTO.setContactDetails(leaveApplication.getContactDetails());
        leaveApplicationDTO.setWorkDissemination(leaveApplication.getWorkDissemination());
        // 其他字段设置...
        //System.out.println("userid" + ": " + leaveApplicationDTO.getApplicationId());
        //System.out.println("userid" + ": " + leaveApplicationDTO.getUserID());
        //System.out.println("date: " + leaveApplicationDTO.getApproveDate());
        //System.out.println("status:  " + leaveApplicationDTO.getStatus());

        return leaveApplicationDTO;
    }

    
    
    public Page<ManagerLeaveApplicationDTO> searchLeave(String username, Integer userId, Pageable pageable) {
        Specification<ManagerLeaveApplication> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (username != null && !username.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.join("user").get("name"), username));
            }
            if (userId != null) {
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page<ManagerLeaveApplication> leaveApplicationPage = managerRepository.findAll(spec, pageable);
        return leaveApplicationPage.map(this::convertToLeaveApplicationDTO);
    }

}
