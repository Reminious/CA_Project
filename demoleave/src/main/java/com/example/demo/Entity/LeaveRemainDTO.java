package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaveRemainDTO {
    private Integer userID;
    private String username;
    private Integer year;
    private Integer AnnualRemain;
    private Integer SickRemain;
    private Integer CompensationRemain;

    public LeaveRemainDTO(Integer userID,String username, Integer year, Integer annualRemain, Integer sickRemain, Integer compensationRemain) {
        this.userID = userID;
        this.username = username;
        this.year = year;
        AnnualRemain = annualRemain;
        SickRemain = sickRemain;
        CompensationRemain = compensationRemain;
    }
    @Override
    public String toString() {
        return "LeaveRemainDTO{" +
                "userID=" + userID +
                "username='" + username + '\'' +
                ", year=" + year +
                ", AnnualRemain=" + AnnualRemain +
                ", SickRemain=" + SickRemain +
                ", CompensationRemain=" + CompensationRemain +
                '}';
    }
}
