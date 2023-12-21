package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.demo.Entity.LeaveApplication;
import com.example.demo.Entity.LeaveRemain;
import com.example.demo.Entity.LeaveRemainId;

public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication,Integer>{
	LeaveApplication findByUserIDAndStatus(Integer UserID, String Status);
}
