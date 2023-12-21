package com.example.demo.Repository;
import com.example.demo.Entity.LeaveApplication;
import com.example.demo.Entity.ManagerLeaveApplication;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerLeaveApplicationRepository extends JpaRepository<ManagerLeaveApplication, Integer> {
    Page<ManagerLeaveApplication> findByApproverID(Integer approverID
    		, Pageable pageable);
    Optional<ManagerLeaveApplication> findByApplicationId(Integer id);
}
