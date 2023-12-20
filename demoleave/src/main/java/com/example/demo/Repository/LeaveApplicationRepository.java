package com.example.demo.Repository;

import com.example.demo.Entity.LeaveApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Integer> {

    List<LeaveApplication> findByUser_Id(Integer id);
    Page<LeaveApplication> findAllByStartDateBetweenAndStatus(LocalDate start, LocalDate end, String status, Pageable pageable);

}
