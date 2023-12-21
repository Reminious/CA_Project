package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "leave_application", schema = "leavedb")
public class LeaveApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ApplicationID", nullable = false)
    private Integer id;

    @Column(name = "UserID", nullable = false)
    private Integer userID;
    
    @Column(name = "LeaveType", nullable = false, length = 50)
    private String leaveType;

    @Column(name = "Reason", length = 500)
    private String reason;

    @Column(name = "StartDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "EndDate", nullable = false)
    private LocalDate endDate;

    @Column(name = "Status", nullable = false, length = 50)
    private String status;

    @Column(name = "SubmitDate", nullable = false)
    private Instant submitDate;

    @Column(name = "ApproverID")
    private Integer approverID;

    @Column(name = "ApproveDate")
    private Instant approveDate;

    @Column(name = "Comment", length = 500)
    private String comment;
    
    @Column(name = "contact_details", length = 50)
    private String contact_details;

    @Column(name = "work_dissemination", length = 50)
    private String work_dissemination;
    public Integer getStartYear() {
        return startDate.getYear();
    }
}