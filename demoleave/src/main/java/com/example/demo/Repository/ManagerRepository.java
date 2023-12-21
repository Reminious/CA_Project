package com.example.demo.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.Entity.LeaveApplication;
import com.example.demo.Entity.LeaveRemainId;
import com.example.demo.Entity.ManagerLeaveApplication;

public interface ManagerRepository extends JpaRepository<ManagerLeaveApplication,Integer>{}
//需要修正