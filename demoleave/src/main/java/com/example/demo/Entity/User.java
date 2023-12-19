package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user", schema = "leavedb")
public class User implements java.io.Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID", nullable = false)
    private Integer id;

    @Column(name = "Name", nullable = false, length = 50)
    private String name;

    @Column(name = "Password", nullable = false, length = 50)
    private String password;

    @Column(name = "JobTitle", nullable = false, length = 50)
    private String jobTitle;

    @Column(name = "Supervisor")
    private Integer supervisor;
}