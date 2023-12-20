package com.example.demo.Service;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.Entity.LeaveRemain;
import com.example.demo.Entity.LeaveRemainId;
import com.example.demo.Repository.AdminRepository;
@Service
@Transactional
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public LeaveRemain addLeave(Integer userId, Integer year, Integer AnnualRemain, Integer SickRemain, Integer CompensationRemain) {
        LeaveRemainId leaveRemainId = new LeaveRemainId();
        leaveRemainId.setUserID(userId);
        leaveRemainId.setYear(year);

        LeaveRemain newLeaveRemain = new LeaveRemain();
        newLeaveRemain.setId(leaveRemainId);
        newLeaveRemain.setAnnualRemain(AnnualRemain);
        newLeaveRemain.setSickRemain(SickRemain);
        newLeaveRemain.setCompensationRemain(CompensationRemain);
        return adminRepository.save(newLeaveRemain);
    }
}




