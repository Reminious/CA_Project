package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaveFormDTO {
    private Integer userId;
    private Integer year;
    private Integer annualRemain;
    private Integer sickRemain;
    private Integer compensationRemain;

    public LeaveFormDTO(Integer userId, Integer year, Integer annualRemain, Integer sickRemain, Integer compensationRemain) {
        this.userId = userId;
        this.year = year;
        this.annualRemain = annualRemain;
        this.sickRemain = sickRemain;
        this.compensationRemain = compensationRemain;
    }
}
